/**
 * (C) Copyright 2019 Ildar Ishalin
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
import kotlinx.coroutines.coroutineScope
import kz.ildar.sandbox.R
import kz.ildar.sandbox.utils.FormatResourceString
import kz.ildar.sandbox.utils.IdResourceString
import kz.ildar.sandbox.utils.ResourceString
import kz.ildar.sandbox.utils.TextResourceString
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.net.ConnectException
import java.net.HttpURLConnection.HTTP_INTERNAL_ERROR
import java.net.HttpURLConnection.HTTP_NOT_FOUND
import java.net.SocketTimeoutException

interface CoroutineCaller {
    suspend fun <T> apiCall(
        customErrorHandler: ((ErrorResponse) -> RequestResult.Error)? = null,
        request: suspend () -> T
    ): RequestResult<T>

    suspend fun <T> apiCallStrict(
        customErrorHandler: ((ErrorResponse) -> RequestResult.Error)? = null,
        request: suspend () -> T
    ): T
}

interface MultiCoroutineCaller {
    suspend fun <T> multiCall(vararg requests: T): List<RequestResult<T>>

    suspend fun <T1, T2, R> zip(
        request1: suspend () -> T1,
        request2: suspend () -> T2,
        zipper: (RequestResult<T1>, RequestResult<T2>) -> R
    ): R

    suspend fun <T1, T2, T3, R> zip(
        request1: suspend () -> T1,
        request2: suspend () -> T2,
        request3: suspend () -> T3,
        zipper: (RequestResult<T1>, RequestResult<T2>, RequestResult<T3>) -> R
    ): R

    suspend fun <T, R> zipArray(
        vararg requests: suspend () -> T,
        zipper: (List<RequestResult<T>>) -> R
    ): R
}

interface ApiCallerInterface : CoroutineCaller, MultiCoroutineCaller

object ApiCaller : ApiCallerInterface {

    private const val HTTP_CODE_ACCOUNT_BLOCKED = 419

    /**
     * Обработчик для однородных запросов на `kotlin coroutines`
     * [requests] должны возвращать один тип данных
     * запускает все [requests] и записывает их в массив [RequestResult]
     * обрабатывает ошибки сервера при помощи [apiCall]
     * обрабатывает ошибки соединения при помощи [apiCall]
     */
    override suspend fun <T> multiCall(vararg requests: T) = requests.map { apiCall { it } }

    /**
     * Обработчик для однородных запросов на `kotlin coroutines`
     * [requests] должны возвращать один тип данных
     * запускает все [requests], записывает их в массив [RequestResult]
     * и передает в обработчик [zipper]
     * обрабатывает ошибки сервера при помощи [apiCall]
     * обрабатывает ошибки соединения при помощи [apiCall]
     */
    override suspend fun <T, R> zipArray(
        vararg requests: suspend () -> T,
        zipper: (List<RequestResult<T>>) -> R
    ) = zipper(requests.map { apiCall(request = it) })

    /**
     * Обработчик для двух разнородных запросов на `kotlin coroutines`
     * запускает [request1], [request2] и передает в обработчик [zipper]
     * обрабатывает ошибки сервера при помощи [apiCall]
     * обрабатывает ошибки соединения при помощи [apiCall]
     */
    override suspend fun <T1, T2, R> zip(
        request1: suspend () -> T1,
        request2: suspend () -> T2,
        zipper: (RequestResult<T1>, RequestResult<T2>) -> R
    ): R = zipper(apiCall(request = request1), apiCall(request = request2))

    /**
     * Обработчик для трех разнородных запросов на `kotlin coroutines`
     * запускает [request1], [request2], [request3] и передает в обработчик [zipper]
     * обрабатывает ошибки сервера при помощи [apiCall]
     * обрабатывает ошибки соединения при помощи [apiCall]
     */
    override suspend fun <T1, T2, T3, R> zip(
        request1: suspend () -> T1,
        request2: suspend () -> T2,
        request3: suspend () -> T3,
        zipper: (RequestResult<T1>, RequestResult<T2>, RequestResult<T3>) -> R
    ): R = zipper(apiCall(request = request1), apiCall(request = request2), apiCall(request = request3))

    /**
     * Обработчик запросов на `kotlin coroutines`
     * ждет выполнения запроса [request]
     * обрабатывает ошибки сервера и соединения
     * возвращает [RequestResult.Success] или [RequestResult.Error]
     * Применяется для suspend-функций Retrofit-api
     */
    override suspend fun <T> apiCall(
        customErrorHandler: ((ErrorResponse) -> RequestResult.Error)?,
        request: suspend () -> T
    ): RequestResult<T> = try {
        coroutineScope {
            RequestResult.Success(request.invoke())
        }
    } catch (e: Exception) {
        handleException(e, customErrorHandler)
    }

    /**
     * Обработчик запросов на `kotlin coroutines`
     * ждет выполнения запроса [request]
     * обрабатывает ошибки сервера и соединения
     * возвращает готовое значение [T]
     * Применяется для suspend-функций Retrofit-api
     *
     * Бросает исключение вместо возвращения ошибки
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

    private fun handleException(
        e: Exception,
        customErrorHandler: ((ErrorResponse) -> RequestResult.Error)? = null
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
        is HttpException -> when (e.code()) {
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
                } else
                    RequestResult.Error(IdResourceString(R.string.request_http_error_500), e.code())
            }
            else -> {
                val error = ErrorResponse.from(e.response()?.errorBody())
                if (customErrorHandler != null && error != null) {
                    customErrorHandler.invoke(error)
                } else {
                    RequestResult.Error(
                        error.print(FormatResourceString(R.string.request_http_error_format, e.code()))
                    )
                }
            }
        }
        else -> {
            RequestResult.Error(FormatResourceString(R.string.request_error, e::class.java.simpleName, e.localizedMessage))
        }
    }
}

/**
 * Презентация ответов сервера для `Presentation layer`
 * должно возвращаться репозиториями, использующими [ApiCaller]
 */
sealed class RequestResult<out T : Any?> {
    data class Success<out T : Any?>(val result: T) : RequestResult<T>()
    data class Error(val error: ResourceString, val code: Int = 0) : RequestResult<Nothing>()
}

/**
 * Исключение, которое можно использовать вместе с [ApiCaller.apiCallStrict]
 */
class RequestError(val error: ResourceString, val code: Int = 0) : Exception()

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
            response?.close()
        }
    }
}

/**
 * [ErrorResponse.print] for nullable type
 */
fun ErrorResponse?.print(
    default: ResourceString
): ResourceString = this?.print(default) ?: default
