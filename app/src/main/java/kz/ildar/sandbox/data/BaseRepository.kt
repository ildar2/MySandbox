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
import java.net.SocketTimeoutException

interface CoroutineCaller {
    suspend fun <T> coroutineApiCall(deferred: Deferred<Response<T>>): RequestResult<T>
}

interface MultiCoroutineCaller {
    suspend fun <T : Any> multiCall(vararg requests: Deferred<Response<T>>): List<RequestResult<T>>
}

interface ApiCallerInterface : CoroutineCaller, MultiCoroutineCaller

class ApiCaller : ApiCallerInterface {

    /**
     * Обработчик для нескольких запросов на `kotlin coroutines`
     * запускает все [requests] и записывает их в массив [RequestResult]
     * обрабатывает ошибки сервера при помощи [coroutineApiCall]
     * обрабатывает ошибки соединения при помощи [coroutineApiCall]
     *   пока есть ограничение: можно делать только однородные запросы
     *   то есть [requests] должны возвращать либо один тип данных, либо общий интерфейс
     */
    override suspend fun <T : Any> multiCall(vararg requests: Deferred<Response<T>>): List<RequestResult<T>> =
        requests.map {
            coroutineApiCall(it)
        }

    /**
     * Обработчик запросов на `kotlin coroutines`
     * ждет выполнения запроса [deferred]
     * обрабатывает ошибки сервера
     * обрабатывает ошибки соединения
     * возвращает [RequestResult.Success] или [RequestResult.Error]
     */
    override suspend fun <T> coroutineApiCall(deferred: Deferred<Response<T>>): RequestResult<T> = try {
        handleResult(deferred.await())
    } catch (e: Exception) {
        Timber.w(e);
        handleException(e)
    }

    private fun <T> handleResult(result: Response<T>): RequestResult<T> = if (result.isSuccessful) {
        RequestResult.Success(result.body())
    } else {
        result.errorBody()?.string().let { errorString ->
            if (!errorString.isNullOrBlank())
                return RequestResult.Error(TextResourceString(ServerError.print(errorString)))
        }
        RequestResult.Error(FormatResourceString(R.string.request_not_successful, result.code()), result.code())
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
            RequestResult.Error(FormatResourceString(R.string.request_http_error, e.code()), e.code())
        }
        else -> {
            RequestResult.Error(FormatResourceString(R.string.request_error, e::class.java.name, e.localizedMessage))
        }
    }
}

/**
 * Презентация ответов сервера для `Presentation layer`
 * должно возвращаться репозиториями, наследующими [ApiCaller]
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

        fun print(response: String): String {
            val error = from(response)
            return "${error.status} ${error.print()}"
        }
    }
}