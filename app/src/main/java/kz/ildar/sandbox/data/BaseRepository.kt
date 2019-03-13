package kz.ildar.sandbox.data

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.Deferred
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketTimeoutException

interface CoroutineCaller {
    suspend fun <T : Any> coroutineApiCall(deferred: Deferred<Response<T>>): RequestResult<T>
}

class BaseRepository : CoroutineCaller {
    /**
     * Обработчик запросов на `kotlin coroutines`
     * ждет выполнения запроса [deferred]
     * обрабатывает ошибки сервера
     * обрабатывает ошибки соединения
     * возвращает [RequestResult.Success] или [RequestResult.Error]
     */
    override suspend fun <T : Any> coroutineApiCall(deferred: Deferred<Response<T>>): RequestResult<T> = try {
        val result = deferred.await()
        if (result.isSuccessful) {
            RequestResult.Success(result.body())
        } else {
            val errorBody = result.errorBody()
            errorBody?.let {
                return RequestResult.Error(ServerError.print(errorBody.string()))
            }
            RequestResult.Error("request was not successful")
        }
    } catch (e: Exception) {
        Timber.w(e, "coroutineApiCall failed");
        when (e) {
            is JsonSyntaxException -> {
                RequestResult.Error("Ошибка обработки запроса")
            }
            is ConnectException -> {
                RequestResult.Error("Проверьте подключение к интернету")
            }
            is SocketTimeoutException -> {
                RequestResult.Error("Сервер не отвечает")
            }
            is HttpException -> {
                RequestResult.Error("Ошибка запроса: ${e.code()}")
            }
            else -> {
                RequestResult.Error("Ошибка: ${e::class.java.name}\n${e.localizedMessage}")
            }
        }
    }
}

/**
 * Презентация ответов сервера для `Presentation layer`
 * должно возвращаться репозиториями, наследующими [BaseRepository]
 */
sealed class RequestResult<out T : Any?> {
    data class Success<out T : Any?>(val result: T? = null) : RequestResult<T>()
    data class Error(val error: String, val code: Int = 0) : RequestResult<Nothing>()
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