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

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kz.ildar.sandbox.di.CoroutineProvider
import kz.ildar.sandbox.utils.EventWrapper
import kz.ildar.sandbox.utils.ResourceString

abstract class BaseViewModel(
    private val contextProvider: CoroutineProvider = CoroutineProvider(),
    private val coroutineJob: Job = Job(),
    protected val scope: CoroutineScope = CoroutineScope(coroutineJob + contextProvider.IO),
    private val _statusLiveData: MutableLiveData<Status> = MutableLiveData(),
    private val _errorLiveData: MutableLiveData<EventWrapper<ResourceString>> = MutableLiveData(),
    protected val uiCaller: UiCaller = UiCallerImpl(scope, contextProvider, _statusLiveData, _errorLiveData)
) : ViewModel(), UiProvider by uiCaller {
    override fun onCleared() {
        super.onCleared()
        coroutineJob.cancel()
    }
}