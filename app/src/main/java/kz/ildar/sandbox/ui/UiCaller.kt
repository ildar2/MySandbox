package kz.ildar.sandbox.ui

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kz.ildar.sandbox.data.RequestResult
import kz.ildar.sandbox.di.CoroutineContextProvider
import kz.ildar.sandbox.utils.Event
import kz.ildar.sandbox.utils.ResourceString

interface UiCaller {
    fun <T> makeRequest(
        call: suspend CoroutineScope.() -> T,
        resultBlock: (suspend (T) -> Unit)? = null
    ): Job

    fun <T> unwrap(
        result: RequestResult<T>,
        errorBlock: ((ResourceString) -> Unit)? = { setError(it) },
        successBlock: (T) -> Unit
    ): Unit?

    fun set(status: BaseViewModel.Status)

    fun setError(error: ResourceString)
}

class UiCallerImpl(
    val scope: CoroutineScope,
    val scopeProvider: CoroutineContextProvider,
    val statusLiveData: MutableLiveData<BaseViewModel.Status>,
    val _errorLiveData: MutableLiveData<Event<ResourceString>>
) : UiCaller {
    /**
     * Presentation-layer-обработчик для запросов через `kotlin coroutines`:
     * запускает [Job] в [scope],
     * вызывает прогресс на [statusLiveData] или [view]
     *
     * [call] - `suspend`-функция запроса из репозитория
     * [resultBlock] - функция, которую нужно выполнить по завершении запроса в UI-потоке
     */
    override fun <T> makeRequest(
        call: suspend CoroutineScope.() -> T,
        resultBlock: (suspend (T) -> Unit)?
    ) = scope.launch(scopeProvider.main) {
        set(BaseViewModel.Status.SHOW_LOADING)
        val result = withContext(scopeProvider.io, call)
        resultBlock?.invoke(result)
        set(BaseViewModel.Status.HIDE_LOADING)
    }

    override fun set(status: BaseViewModel.Status) {
        statusLiveData.postValue(status)
    }

    override fun setError(error: ResourceString) {
        _errorLiveData.postValue(Event(error))
    }

    /**
     * Обработчик для ответов [RequestResult] репозитория.
     * [errorBlock] - функция обработки ошибок, можно передать `null`, чтобы никак не обрабатывать.
     * [successBlock] - обработка непустого результата
     */
    override fun <T> unwrap(
        result: RequestResult<T>,
        errorBlock: ((ResourceString) -> Unit)?,
        successBlock: (T) -> Unit
    ) = when (result) {
        is RequestResult.Success -> result.result?.let { successBlock(it) }
        is RequestResult.Error -> errorBlock?.invoke(result.error)
    }
}