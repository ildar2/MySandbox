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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kz.ildar.sandbox.di.CoroutineContextProvider
import kz.ildar.sandbox.utils.Event
import kz.ildar.sandbox.utils.ResourceString
import org.koin.standalone.KoinComponent

abstract class BaseViewModel(
    private val contextProvider: CoroutineContextProvider,
    private val coroutineJob: Job = Job(),
    protected val scope: CoroutineScope = CoroutineScope(coroutineJob + contextProvider.io),
    val statusLiveData: MutableLiveData<Status> = MutableLiveData(),
    private val _errorLiveData: MutableLiveData<Event<ResourceString>> = MutableLiveData()
) : ViewModel(), KoinComponent, UiCaller by UiCallerImpl(scope, contextProvider, statusLiveData, _errorLiveData) {

    val errorLiveData: LiveData<Event<ResourceString>>
        get() = _errorLiveData

    override fun onCleared() {
        super.onCleared()
        coroutineJob.cancel()
    }

    enum class Status {
        SHOW_LOADING,
        HIDE_LOADING,
        ERROR,
        SUCCESS
    }
}