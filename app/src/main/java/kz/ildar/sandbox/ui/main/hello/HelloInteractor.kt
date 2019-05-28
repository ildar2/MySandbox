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
package kz.ildar.sandbox.ui.main.hello

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kz.ildar.sandbox.data.HelloRepository
import kz.ildar.sandbox.data.RequestResult
import kz.ildar.sandbox.ui.UiCaller
import kz.ildar.sandbox.utils.EventWrapper
import kz.ildar.sandbox.utils.ResourceString
import kz.ildar.sandbox.utils.TextResourceString

interface HelloInteractor {
    val greetingLiveData: LiveData<EventWrapper<ResourceString>>
    fun loadGreetings(name: String)
}

@Deprecated("Used only when local server is available")
class HelloLocalImpl(
    private val repo: HelloRepository,
    private val uiCaller: UiCaller
) : HelloInteractor {

    override val greetingLiveData: MutableLiveData<EventWrapper<ResourceString>> = MutableLiveData()

    override fun loadGreetings(name: String) {
        uiCaller.makeRequest({
            if (name.isBlank()) {
                repo.greetings()
            } else {
                repo.personalGreeting(name)
            }
        }) {
            when (it) {
                is RequestResult.Success -> greetingLiveData.postValue(EventWrapper(TextResourceString(it.result?.getContents())))
                is RequestResult.Error -> uiCaller.setError(it.error)
            }
        }
    }
}

class HelloEchoImpl(
    private val repo: HelloRepository
) : HelloInteractor {
    lateinit var uiCaller: UiCaller

    override val greetingLiveData: MutableLiveData<EventWrapper<ResourceString>> = MutableLiveData()

    override fun loadGreetings(name: String) {
        uiCaller.makeRequest({
            if (name.isBlank()) {
                repo.echoGreetings()
            } else {
                repo.echoPersonalGreeting(name)
            }
        }) {
            when (it) {
                is RequestResult.Success -> greetingLiveData.postValue(EventWrapper(TextResourceString(it.result?.getContents())))
                is RequestResult.Error -> uiCaller.setError(it.error)
            }
        }
    }
}