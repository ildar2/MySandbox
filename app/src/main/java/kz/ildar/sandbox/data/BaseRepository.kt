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

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.Deferred
import kz.ildar.sandbox.R
import kz.ildar.sandbox.utils.FormatResourceString
import kz.ildar.sandbox.utils.IdResourceString
import kz.ildar.sandbox.utils.ResourceString
import kz.ildar.sandbox.utils.TextResourceString
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import java.net.ConnectException
import java.net.HttpURLConnection.HTTP_INTERNAL_ERROR
import java.net.HttpURLConnection.HTTP_NOT_FOUND
import java.net.SocketTimeoutException

interface CoroutineCaller {
    suspend fun <T> coroutineApiCall(deferred: Deferred<Response<T>>): RequestResult<T>
    suspend fun <T> coroutineApiCallRaw(deferred: Deferred<T>): RequestResult<T>
}

interface MultiCoroutineCaller {
    suspend fun <T> multiCall(vararg requests: Deferred<Response<T>>): List<RequestResult<T>>

    suspend fun <T1, T2, R> zip(
        request1: Deferred<Response<T1>>,
        request2: Deferred<Response<T2>>,
        zipper: (RequestResult<T1>, RequestResult<T2>) -> R
    ): R

    suspend fun <T1, T2, T3, R> zip(
        request1: Deferred<Response<T1>>,
        request2: Deferred<Response<T2>>,
        request3: Deferred<Response<T3>>,
        zipper: (RequestResult<T1>, RequestResult<T2>, RequestResult<T3>) -> R
    ): R

    suspend fun <T, R> zipArray(
        vararg requests: Deferred<Response<T>>,
        zipper: (List<RequestResult<T>>) -> R
    ): R
}

interface ApiCallerInterface : CoroutineCaller, MultiCoroutineCaller

class ApiCaller : ApiCallerInterface {

    /**
     * Обработчик для однородных запросов на `kotlin coroutines`
     * [requests] должны возвращать один тип данных
     * запускает все [requests] и записывает их в массив [RequestResult]
     * обрабатывает ошибки сервера при помощи [coroutineApiCall]
     * обрабатывает ошибки соединения при помощи [coroutineApiCall]
     */
    override suspend fun <T> multiCall(vararg requests: Deferred<Response<T>>): List<RequestResult<T>> =
        requests.map {
            coroutineApiCall(it)
        }

    /**
     * Обработчик для однородных запросов на `kotlin coroutines`
     * [requests] должны возвращать один тип данных
     * запускает все [requests], записывает их в массив [RequestResult]
     * и передает в обработчик [zipper]
     * обрабатывает ошибки сервера при помощи [coroutineApiCall]
     * обрабатывает ошибки соединения при помощи [coroutineApiCall]
     */
    override suspend fun <T, R> zipArray(
        vararg requests: Deferred<Response<T>>,
        zipper: (List<RequestResult<T>>) -> R
    ): R = zipper(requests.map { coroutineApiCall(it) })

    /**
     * Обработчик для двух разнородных запросов на `kotlin coroutines`
     * запускает [request1], [request2] и передает в обработчик [zipper]
     * обрабатывает ошибки сервера при помощи [coroutineApiCall]
     * обрабатывает ошибки соединения при помощи [coroutineApiCall]
     */
    override suspend fun <T1, T2, R> zip(
        request1: Deferred<Response<T1>>,
        request2: Deferred<Response<T2>>,
        zipper: (RequestResult<T1>, RequestResult<T2>) -> R
    ): R = zipper(coroutineApiCall(request1), coroutineApiCall(request2))

    /**
     * Обработчик для трех разнородных запросов на `kotlin coroutines`
     * запускает [request1], [request2], [request3] и передает в обработчик [zipper]
     * обрабатывает ошибки сервера при помощи [coroutineApiCall]
     * обрабатывает ошибки соединения при помощи [coroutineApiCall]
     */
    override suspend fun <T1, T2, T3, R> zip(
        request1: Deferred<Response<T1>>,
        request2: Deferred<Response<T2>>,
        request3: Deferred<Response<T3>>,
        zipper: (RequestResult<T1>, RequestResult<T2>, RequestResult<T3>) -> R
    ): R = zipper(coroutineApiCall(request1), coroutineApiCall(request2), coroutineApiCall(request3))

    /**
     * Обработчик запросов, обернутых в [Response] на `kotlin coroutines`
     * ждет выполнения запроса [deferred]
     * обрабатывает ошибки сервера и соединения
     * возвращает [RequestResult.Success] или [RequestResult.Error]
     */
    override suspend fun <T> coroutineApiCall(deferred: Deferred<Response<T>>): RequestResult<T> = try {
        handleResult(deferred.await())
    } catch (e: Exception) {
        Timber.w(e);
        handleException(e)
    }

    /**
     * Обработчик запросов на `kotlin coroutines`
     * ждет выполнения запроса [deferred]
     * обрабатывает ошибки сервера и соединения
     * возвращает [RequestResult.Success] или [RequestResult.Error]
     */
    override suspend fun <T> coroutineApiCallRaw(deferred: Deferred<T>): RequestResult<T> = try {
        RequestResult.Success(deferred.await())
    } catch (e: Exception) {
        Timber.w(e);
        handleException(e)
    }

    private fun <T> handleResult(result: Response<T>): RequestResult<T> = if (result.isSuccessful) {
        RequestResult.Success(result.body())
    } else {
        throw HttpException(result)
    }

    private fun <T> handleException(e: Exception): RequestResult<T> = when (e) {
        is JsonSyntaxException -> {
            RequestResult.Error(IdResourceString(R.string.request_json_error))
        }
        is ConnectException -> {
            RequestResult.Error(IdResourceString(R.string.request_connection_error))
        }
        is SocketTimeoutException -> {
            RequestResult.Error(IdResourceString(R.string.request_timeout))
        }
        is HttpException -> {
            when (e.code()) {
                HTTP_NOT_FOUND -> {
                    RequestResult.Error(IdResourceString(R.string.request_http_error_404), e.code())
                }
                HTTP_INTERNAL_ERROR -> {
                    RequestResult.Error(IdResourceString(R.string.request_http_error_500), e.code())
                }
                else -> {
                    val errorBody = e.response().errorBody()?.string()
                    if (errorBody.isNullOrBlank()) {
                        RequestResult.Error(FormatResourceString(R.string.request_http_error_format, e.code()))
                    } else {
                        RequestResult.Error(ServerError.wrapPrint(errorBody, e.code()), e.code())
                    }
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
    data class Success<out T : Any?>(val result: T? = null) : RequestResult<T>()
    data class Error(val error: ResourceString, val code: Int = 0) : RequestResult<Nothing>()
}

data class ServerError(
    val timestamp: String?,
    val status: Int,
    val error: String?,
    val message: String?,
    val path: String?
) {

    fun print(): String {
        return message ?: error ?: "ServerError"
    }

    companion object {
        private fun from(response: String): ServerError {
            return Gson().fromJson(response, ServerError::class.java)
        }

        fun print(response: String, code: Int): String {
            val error = from(response)
            return "$code ${error.print()}"
        }

        fun wrapPrint(response: String, code: Int): ResourceString {
            return TextResourceString(print(response, code))
        }
    }
}