package kz.ildar.sandbox.data

import com.google.gson.GsonBuilder
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
            val body = result.body()
            body?.let {
                return RequestResult.Success(body)
            }
            RequestResult.Error("empty body")
        } else {
            val errorBody = result.errorBody()
            errorBody?.let {
                val serverError = GsonBuilder().create().fromJson(it.string(), ServerError::class.java)
                return RequestResult.Error("Сервер вернул ошибку: ${serverError.status}")
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
    data class Success<out T : Any?>(val result: T) : RequestResult<T>()
    data class Error(val error: String) : RequestResult<Nothing>()
}

class ServerError(
    val timestamp: String,
    val status: Int,
    val error: String,
    val message: String,
    val path: String
)
//{"timestamp":"2019-03-02T07:16:48.119+0000","status":404,"error":"Not Found","message":"No message available","path":"/greetings"}