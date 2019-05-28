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
import kz.ildar.sandbox.ui.BaseViewModel

class HelloViewModel(
    private val repo: HelloRepository,
    private val helloDelegate: HelloEchoImpl = HelloEchoImpl(repo)
) : BaseViewModel(), HelloInteractor by helloDelegate {
    init {
        helloDelegate.uiCaller = uiCaller
    }
}