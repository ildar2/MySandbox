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
import kz.ildar.sandbox.ui.BaseViewModel
import kz.ildar.sandbox.utils.Event
import kz.ildar.sandbox.utils.ResourceString
import kz.ildar.sandbox.utils.TextResourceString
import timber.log.Timber

class HelloViewModel(private val repo: HelloRepository) : BaseViewModel() {

    private val _greetingLiveData = MutableLiveData<Event<ResourceString>>()
    internal val greetingLiveData: LiveData<Event<ResourceString>>
        get() = _greetingLiveData

    fun loadGreetings(name: String) {
        Timber.w("loadGreetings called")
        if (name.isBlank()) {
            loadEchoGreeting()
        } else {
            loadEchoPersonalGreeting(name)
        }
    }

    private fun loadEchoGreeting() {
        makeRequest({ repo.echoGreetings() }) {
            unwrap(it) {
                _greetingLiveData.value = Event(TextResourceString(it.args.content))
            }
        }
    }

    private fun loadEchoPersonalGreeting(name: String) {
        makeRequest({ repo.echoPersonalGreeting(name) }) {
            unwrap(it) {
                _greetingLiveData.value = Event(TextResourceString(it.args.content))
            }
        }
    }

    private fun loadGreeting() {
        makeRequest({ repo.greetings() }) {
            when (it) {
                is RequestResult.Success -> _greetingLiveData.value = Event(TextResourceString(it.result?.content))
                is RequestResult.Error -> setError(it.error)
            }
        }
    }

    private fun loadPersonalGreeting(name: String) {
        makeRequest({ repo.personalGreeting(name) }) {
            when (it) {
                is RequestResult.Success -> _greetingLiveData.value = Event(TextResourceString(it.result?.content))
                is RequestResult.Error -> setError(it.error)
            }
        }
    }
}