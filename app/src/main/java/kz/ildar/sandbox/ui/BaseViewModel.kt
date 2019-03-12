package kz.ildar.sandbox.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kz.ildar.sandbox.data.RequestResult
import kz.ildar.sandbox.di.CoroutineContextProvider
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import retrofit2.Response

abstract class BaseViewModel : ViewModel(), KoinComponent {
    val statusLiveData = MutableLiveData<Status>()
    val errorLiveData = MutableLiveData<String>()

    private val scopeProvider: CoroutineContextProvider by inject()

    private val coroutineJob = Job()

    protected val scope = CoroutineScope(coroutineJob + scopeProvider.io)

    override fun onCleared() {
        super.onCleared()
        coroutineJob.cancel()
    }

    /**
     * Presentation-layer-обработчик для запросов через `kotlin coroutines`:
     * запускает [Job] в [scope],
     * вызывает прогресс на [statusLiveData] или [view]
     *
     * [call] - `suspend`-функция запроса из репозитория
     * [resultBlock] - функция, которую нужно выполнить по завершении запроса в UI-потоке
     */
    fun <T> makeRequest(
        call: suspend () -> RequestResult<T>,
        resultBlock: suspend (RequestResult<T>) -> Unit
    ) = scope.launch(scopeProvider.main) {
        this@BaseViewModel set Status.SHOW_LOADING
        val result = withContext(scopeProvider.io) {
            call.invoke()
        }
        resultBlock(result)
        this@BaseViewModel set Status.HIDE_LOADING
    }

    /**
     * Обработчик для запросов через `kotlin coroutines` - запускает [Job] в [scope],
     * вызывает прогресс на [statusLiveData],
     * разворачивает [Response] - вытаскивает тело запроса и вызывает [successBlock] с ним,
     * обрабатывает коды ошибок [Response],
     * обрабатывает исключения во время исполнения [deferred]
     */
    @Deprecated("обработка ошибок и исключений должна проходить на уровне репозитория")
    fun <T> makeRequestWrong(deferred: Deferred<Response<T>>, successBlock: suspend (T) -> Unit) = scope.launch {
        this@BaseViewModel set Status.SHOW_LOADING
        try {
            val result = deferred.await()
            if (result.isSuccessful) {
                val body = result.body()
                body?.let {
                    withContext(scopeProvider.main) {
                        successBlock(it)
                    }
                }
            } else {
                when (result.code()) {//todo handle error codes
                    404 -> setError("no data available")
                    else -> setError("default error")
                }
            }
        } catch (e: Exception) {
            println("makeRequest failed with: ${e.message}")
            e.printStackTrace()
            set(Status.ERROR)//todo порядок сообщений в statusLiveData
            setError("there was an error during request")//todo exception handling
        }
        this@BaseViewModel set Status.HIDE_LOADING
    }

    suspend infix fun set(status: Status) = withContext(scopeProvider.main) {
        statusLiveData.value = status
    }

    suspend infix fun setError(error: String) = withContext(scopeProvider.main) {
        errorLiveData.value = error
    }

    enum class Status {
        SHOW_LOADING,
        HIDE_LOADING,
        ERROR,
        SUCCESS
    }
}