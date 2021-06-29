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
package kz.ildar.sandbox.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import kz.ildar.sandbox.data.RequestResult
import kz.ildar.sandbox.di.CoroutineProvider
import kz.ildar.sandbox.utils.EventWrapper
import kz.ildar.sandbox.utils.ResourceString
import kz.ildar.sandbox.utils.TextResourceString

interface UiProvider {
    val statusLiveData: LiveData<Status>
    val errorLiveData: LiveData<EventWrapper<ResourceString>>
}

interface UiCaller : UiProvider {
    override val statusLiveData: MutableLiveData<Status>

    fun <T> makeRequest(
        call: suspend CoroutineScope.() -> T,
        statusLD: MutableLiveData<Status>? = statusLiveData,
        resultBlock: (suspend (T) -> Unit)? = null
    ): Job

    fun <T> unwrap(
        result: RequestResult<T>,
        errorBlock: ((ResourceString) -> Unit)? = { setError(it) },
        successBlock: (T) -> Unit
    ): Unit?

    fun set(status: Status, statusLD: MutableLiveData<Status>? = statusLiveData)

    fun setError(error: ResourceString)
}

class UiCallerImpl(
    private val scope: CoroutineScope,
    private val scopeProvider: CoroutineProvider,
    _statusLiveData: MutableLiveData<Status>,
    _errorLiveData: MutableLiveData<EventWrapper<ResourceString>>
) : UiCaller {
    override val statusLiveData: MutableLiveData<Status> = _statusLiveData
    override val errorLiveData: MutableLiveData<EventWrapper<ResourceString>> = _errorLiveData

    /**
     * Presentation-layer-обработчик для запросов через `kotlin coroutines`:
     * запускает [Job] в [scope],
     * вызывает прогресс на [statusLiveData]
     *
     * [call] - `suspend`-функция запроса из репозитория
     * [statusLD] - можно подставлять разные liveData для разных прогрессов (или null)
     * [resultBlock] - функция, которую нужно выполнить по завершении запроса в UI-потоке
     *
     * возвращает [Job], чтобы можно было прервать запрос
     */
    override fun <T> makeRequest(
        call: suspend CoroutineScope.() -> T,
        statusLD: MutableLiveData<Status>?,
        resultBlock: (suspend (T) -> Unit)?
    ): Job = scope.launch(scopeProvider.Main) {
        set(Status.SHOW_LOADING, statusLD)
        try {
            val result = withContext(scopeProvider.IO, call)
            resultBlock?.invoke(result)
        } catch (e: Exception) {
            if (e !is CancellationException) setError(TextResourceString(e.message.orEmpty()))
        }
        set(Status.HIDE_LOADING, statusLD)
    }

    /**
     * Чтобы не терять прогрессбар на нескольких запросах
     */
    private var requestCounter = 0

    /**
     * Выставляем статус
     * по дефолту выставлен [statusLiveData]
     * можно подставить свою лайвдату или [null]
     */
    override fun set(status: Status, statusLD: MutableLiveData<Status>?) {
        statusLD ?: return
        if (statusLD === statusLiveData) {
            when (status) {
                Status.SHOW_LOADING -> {
                    requestCounter++
                }
                Status.HIDE_LOADING -> {
                    requestCounter--
                    if (requestCounter > 0) return
                    requestCounter = 0
                }
            }
        }
        scope.launch(scopeProvider.Main) {
            statusLD.value = status
        }
    }

    override fun setError(error: ResourceString) {
        scope.launch(scopeProvider.Main) {
            errorLiveData.value = EventWrapper(error)
        }
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
        is RequestResult.Success -> successBlock(result.result)
        is RequestResult.Error -> errorBlock?.invoke(result.error)
    }
}