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
package kz.ildar.sandbox.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kz.ildar.sandbox.data.RequestResult
import kz.ildar.sandbox.di.CoroutineContextProvider
import kz.ildar.sandbox.utils.Event
import kz.ildar.sandbox.utils.ResourceString
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

abstract class BaseViewModel : ViewModel(), KoinComponent, UiCaller {
    val statusLiveData = MutableLiveData<Status>()

    private val _errorLiveData = MutableLiveData<Event<ResourceString>>()
    val errorLiveData: LiveData<Event<ResourceString>>
        get() = _errorLiveData

    private val scopeProvider: CoroutineContextProvider by inject()

    private val coroutineJob = Job()

    protected val scope = CoroutineScope(coroutineJob + scopeProvider.io)

    override fun onCleared() {
        super.onCleared()
        coroutineJob.cancel()
    }

    private val caller: UiCaller = UiCallerImpl(scope, scopeProvider, statusLiveData, _errorLiveData)

    override fun <T> makeRequest(call: suspend CoroutineScope.() -> T, resultBlock: (suspend (T) -> Unit)?) =
        caller.makeRequest(call, resultBlock)

    override fun <T> unwrap(result: RequestResult<T>, errorBlock: ((ResourceString) -> Unit)?, successBlock: (T) -> Unit) =
        caller.unwrap(result, errorBlock, successBlock)

    override fun set(status: Status) = caller.set(status)

    override fun setError(error: ResourceString) = caller.setError(error)

    enum class Status {
        SHOW_LOADING,
        HIDE_LOADING,
        ERROR,
        SUCCESS
    }
}