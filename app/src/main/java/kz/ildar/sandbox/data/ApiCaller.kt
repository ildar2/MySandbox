/**
 * (C) Copyright 2021 Ildar Ishalin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package kz.ildar.sandbox.data

import androidx.annotation.Keep
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.*
import kz.ildar.sandbox.R
import kz.ildar.sandbox.utils.FormatResourceString
import kz.ildar.sandbox.utils.IdResourceString
import kz.ildar.sandbox.utils.ResourceString
import kz.ildar.sandbox.utils.TextResourceString
import okhttp3.Headers
import okhttp3.ResponseBody
import okhttp3.internal.closeQuietly
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import java.net.ConnectException
import java.net.HttpURLConnection.HTTP_INTERNAL_ERROR
import java.net.HttpURLConnection.HTTP_NOT_FOUND
import java.net.SocketTimeoutException
import kotlin.math.pow

/**
 * Интерфейс для репозиториев, указывающий на то, что
 * производится обработка ошибок через [ApiCaller]
 */
interface CoroutineCaller {
    suspend fun <T> apiCall(
        customErrorHandler: ((ErrorResponse) -> RequestResult.Error)? = null,
        request: suspend () -> T
    ): RequestResult<T>

    suspend fun <T> apiCallStrict(
        customErrorHandler: ((ErrorResponse) -> RequestResult.Error)? = null,
        request: suspend () -> T
    ): T

    suspend fun <T> apiCallRetryable(
        customErrorHandler: ((ErrorResponse) -> RequestResult.Error)? = null,
        shouldRetry: Boolean = true,
        request: suspend (retryNumber: Int?, lastStatusCode: Int?) -> Response<T>
    ): RequestResult<T>

    suspend fun <T> apiCallRetryableStrict(
        customErrorHandler: ((ErrorResponse) -> RequestResult.Error)? = null,
        shouldRetry: Boolean = true,
        request: suspend (retryNumber: Int?, lastStatusCode: Int?) -> Response<T>
    ): T
}

object ApiCaller : CoroutineCaller {

    private const val HTTP_CODE_ACCOUNT_BLOCKED = 419

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, exception ->
        val res = handleException(exception)
        println("uncaught: " + res.error)
    }

    /**
     * Обработчик запросов на `kotlin coroutines`
     * ждет выполнения запроса [request]
     * обрабатывает ошибки сервера и соединения
     * возвращает [RequestResult.Success] или [RequestResult.Error]
     * Применяется для suspend-функций Retrofit-api
     * можно добавить обработчик [customErrorHandler]
     */
    override suspend fun <T> apiCall(
        customErrorHandler: ((ErrorResponse) -> RequestResult.Error)?,
        request: suspend () -> T
    ): RequestResult<T> {
        return runCatching {
            val supervisor = SupervisorJob()
            withContext(supervisor + CoroutineExceptionHandler { _, exception ->
                throw exception
            }) {
                async(supervisor) { request.invoke() }.await()
            }
        }.fold(
            onSuccess = { RequestResult.Success(it) },
            onFailure = {
                handleException(it, customErrorHandler)
            }
        )


//        return runCatching { request.invoke() }.fold(
//            onSuccess = { RequestResult.Success(it) },
//            onFailure = {
//                handleException(it, customErrorHandler)
//            }
//        )

//        return try {
//            coroutineScope {
//                RequestResult.Success(request.invoke())
//            }
//        } catch (e: Throwable) {
//            handleException(e, customErrorHandler)
//        }
    }

    /**
     * Обработчик запросов на `kotlin coroutines`
     * ждет выполнения запроса [request]
     * обрабатывает ошибки сервера и соединения
     * возвращает готовое значение [T]
     * Применяется для suspend-функций Retrofit-api
     *
     * Бросает исключение вместо возвращения ошибки
     * можно добавить обработчик [customErrorHandler]
     */
    override suspend fun <T> apiCallStrict(
        customErrorHandler: ((ErrorResponse) -> RequestResult.Error)?,
        request: suspend () -> T
    ): T = try {
        coroutineScope { request.invoke() }
    } catch (e: Exception) {
        val (message, code) = handleException(e, customErrorHandler)
        throw RequestError(message, code)
    }

    /**
     * Общая функция для запросов в сеть с возможностью повторить запрос
     *
     * @param request - запрос в ретрофит. получает параметры для отправки на сервер
     * @param customErrorHandler - кастомный обработчик ошибок
     * @param shouldRetry - нужно ли ретраить запрос
     *
     * @return sealed-класс результата или ошибки
     */
    override suspend fun <T> apiCallRetryable(
        customErrorHandler: ((ErrorResponse) -> RequestResult.Error)?,
        shouldRetry: Boolean,
        request: suspend (retryNumber: Int?, lastStatusCode: Int?) -> Response<T>
    ): RequestResult<T> = apiCallInternal(
        customErrorHandler,
        shouldRetry,
        null,
        null,
        request
    )

    /**
     * Обработчик запросов на `kotlin coroutines`
     * с возможностью повтора запроса
     * ждет выполнения запроса [request]
     * обрабатывает ошибки сервера и соединения
     * возвращает готовое значение [T]
     * Применяется для suspend-функций Retrofit-api
     *
     * Бросает исключение вместо возвращения ошибки
     */
    override suspend fun <T> apiCallRetryableStrict(
        customErrorHandler: ((ErrorResponse) -> RequestResult.Error)?,
        shouldRetry: Boolean,
        request: suspend (retryNumber: Int?, lastStatusCode: Int?) -> Response<T>
    ): T = when (val result = apiCallInternal(
        customErrorHandler,
        shouldRetry,
        null,
        null,
        request
    )) {
        is RequestResult.Success -> result.result
        is RequestResult.Error -> throw RequestError(result.error, result.code)
    }

    private suspend fun <T> apiCallInternal(
        customErrorHandler: ((ErrorResponse) -> RequestResult.Error)? = null,
        shouldRetry: Boolean = false,
        attempt: Int? = null,
        lastErrorCode: Int? = null,
        request: suspend (Int?, Int?) -> Response<T>
    ): RequestResult<T> = try {
        coroutineScope {
            val response = request.invoke(attempt, lastErrorCode)
            if (!response.isSuccessful) {
                throw HttpException(response)
            }
            RequestResult.Success(response.body()!!)
        }
    } catch (e: Throwable) {
        val error = handleException(e, customErrorHandler, attempt ?: 0)
        if (shouldRetry && error.retryAction != null && error.retryAction != RetryAction.Stop) {
            delay(error.retryAction.retryDelayMs())
            apiCallInternal(
                customErrorHandler,
                shouldRetry,
                (attempt ?: 0) + 1,
                error.code,
                request,
            )
        } else {
            error
        }
    }

    fun handleException(
        e: Throwable,
        customErrorHandler: ((ErrorResponse) -> RequestResult.Error)? = null,
        attempt: Int = 0,
    ): RequestResult.Error = when (e) {
        is JsonSyntaxException -> {
            RequestResult.Error(IdResourceString(R.string.request_json_error))
        }
        is ConnectException -> {
            RequestResult.Error(IdResourceString(R.string.request_connection_error))
        }
        is SocketTimeoutException -> {
            RequestResult.Error(IdResourceString(R.string.request_timeout))
        }
        is CancellationException -> {
            Timber.i("CancellationException")
            throw e
        }
        is RequestError -> {
            RequestResult.Error(e.error, e.code)
        }
        is HttpException -> when (e.code()) {
            429, 404 -> {//can add 404 for echo check
                Timber.w("Retryable error on attempt $attempt")
                RequestResult.Error(
                    TextResourceString("Retryable"),
                    e.code(),
                    RetryAction.of(e.response()?.headers(), attempt)
                )
            }
            HTTP_NOT_FOUND -> {
                RequestResult.Error(IdResourceString(R.string.request_http_error_404), e.code())
            }
            HTTP_INTERNAL_ERROR -> {
                val error = ErrorResponse.from(e.response()?.errorBody())
                if (error?.error == "user blocked") {
                    RequestResult.Error(
                        IdResourceString(R.string.request_http_error_user_blocked),
                        HTTP_CODE_ACCOUNT_BLOCKED
                    )
                } else RequestResult.Error(
                    IdResourceString(R.string.request_http_error_500), e.code()
                )
            }
            else -> {
                val error = ErrorResponse.from(e.response()?.errorBody())
                if (customErrorHandler != null && error != null) {
                    customErrorHandler.invoke(error)
                } else {
                    RequestResult.Error(
                        error.print(default = FormatResourceString(
                            R.string.request_http_error_format,
                            e.code()
                        )),
                        e.code()
                    )
                }
            }
        }
        else -> {
            Timber.e(e, "Request failed")
            RequestResult.Error(
                FormatResourceString(
                    R.string.request_error,
                    e::class.java.simpleName,
                    e.localizedMessage.orEmpty(),
                )
            )
        }
    }
}

/**
 * Презентация ответов сервера для `Presentation layer`
 * должно возвращаться репозиториями, использующими [ApiCaller]
 */
sealed class RequestResult<out T : Any?> {
    data class Success<out T : Any?>(val result: T) : RequestResult<T>()
    data class Error(
        val error: ResourceString,
        val code: Int = 0,
        val retryAction: RetryAction? = null,
    ) : RequestResult<Nothing>()
}

/**
 * Исключение, которое можно использовать вместе с [ApiCaller.apiCallStrict]
 */
class RequestError(
    val error: ResourceString,
    val code: Int = 0
) : Throwable()

sealed interface RetryAction {
    fun retryDelayMs(): Long = 0

    companion object {
        fun of(
            headers: Headers?,
            attempt: Int = 0,
            maxClientRetries: Int = 3,
        ): RetryAction = when (headers?.get("Header-Retry-Action")) {
            null -> defaultRetryInterval(attempt, maxClientRetries)
            "stop" -> Stop
            else -> {
                val retryInterval = headers["Header-Retry-Interval-Ms"]?.toLongOrNull()

                when {
                    retryInterval != null && retryInterval > 0 -> RetryInterval(retryInterval)
                    else -> defaultRetryInterval(attempt, maxClientRetries)
                }
            }
        }

        private fun defaultRetryInterval(
            attempt: Int,
            maxClientRetries: Int
        ): RetryAction = when (attempt) {
            in 0 until maxClientRetries -> RetryInterval(1000 * 2.0.pow(attempt).toLong())
            else -> Stop
        }
    }

    object Stop : RetryAction

    class RetryInterval(
        private val delay: Long
    ) : RetryAction {
        override fun retryDelayMs(): Long = delay
    }
}

@Keep
data class ErrorResponse(
    val timestamp: String?,
    val status: Int,
    val error: String?,
    val message: String?,
    val path: String?
) {

    fun print(default: ResourceString): ResourceString {
        return TextResourceString(message ?: error ?: return default)
    }

    companion object {
        /**
         * Парсинг ответа сервера вручную в объект [ErrorResponse]
         */
        fun from(response: ResponseBody?): ErrorResponse? = try {
            Gson().fromJson(response?.charStream(), ErrorResponse::class.java)
        } catch (e: Exception) {
            null
        } finally {
            response?.closeQuietly()
        }
    }
}

/**
 * [ErrorResponse.print] for nullable type
 */
fun ErrorResponse?.print(
    default: ResourceString = TextResourceString("error")
): ResourceString = this?.print(default) ?: default
