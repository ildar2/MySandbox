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
package kz.ildar.sandbox.ui.main.multiCall

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kz.ildar.sandbox.data.MultiCallRepository
import kz.ildar.sandbox.data.RequestResult
import kz.ildar.sandbox.di.CoroutineProvider
import kz.ildar.sandbox.ui.BaseViewModel
import kz.ildar.sandbox.utils.EventWrapper
import kz.ildar.sandbox.utils.ResourceString
import kz.ildar.sandbox.utils.TextResourceString

class MultiCallViewModel(
    private val multiRepo: MultiCallRepository,
    contextProvider: CoroutineProvider
) : BaseViewModel(contextProvider) {

    private val _logLiveData = MutableLiveData<EventWrapper<ResourceString>>()
    internal val logLiveData: LiveData<EventWrapper<ResourceString>>
        get() = _logLiveData

    fun multiCall() {
        makeRequest({ multiRepo.callAllMethods() }) { result ->
            result.forEach {
                _logLiveData.value = when (it) {
                    is RequestResult.Success -> EventWrapper(TextResourceString(it.result?.getContents()))
                    is RequestResult.Error -> EventWrapper(it.error)
                }
            }
        }
    }

    fun twoCall() {
        makeRequest({ multiRepo.callTwoMethods() }) { result ->
            result.forEach {
                _logLiveData.value = when (it) {
                    is RequestResult.Success -> EventWrapper(TextResourceString(it.result?.getContents()))
                    is RequestResult.Error -> EventWrapper(it.error)
                }
            }
        }
    }

    fun threeCall() {
        makeRequest({ multiRepo.callThreeMethods() }) { result ->
            result.forEach {
                _logLiveData.value = when (it) {
                    is RequestResult.Success -> EventWrapper(TextResourceString(it.result?.getContents()))
                    is RequestResult.Error -> EventWrapper(it.error)
                }
            }
        }
    }

    fun arrayCall() {
        makeRequest({ multiRepo.callArrayOfMethods() }) { result ->
            result.forEach {
                _logLiveData.value = when (it) {
                    is RequestResult.Success -> EventWrapper(TextResourceString(it.result?.getContents()))
                    is RequestResult.Error -> EventWrapper(it.error)
                }
            }
        }
    }
}