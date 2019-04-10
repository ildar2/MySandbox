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

import kz.ildar.sandbox.data.HelloRepository
import kz.ildar.sandbox.di.CoroutineProvider
import kz.ildar.sandbox.ui.BaseViewModel
import timber.log.Timber

class HelloViewModel(
    private val repo: HelloRepository,
    contextProvider: CoroutineProvider
) : BaseViewModel(contextProvider),
    HelloInteractor by HelloEchoImpl(repo) {

    fun loadGreetings(name: String) {
        Timber.w("loadGreetings called")
        if (name.isBlank()) {
            makeRequest({ loadGreeting(_errorLiveData) })
        } else {
            makeRequest({ loadPersonalGreeting(name, _errorLiveData) })
        }
    }
}