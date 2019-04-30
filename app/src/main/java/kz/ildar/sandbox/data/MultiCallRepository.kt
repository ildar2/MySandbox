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
package kz.ildar.sandbox.data

import kz.ildar.sandbox.data.api.Api
import kz.ildar.sandbox.data.model.GreetingsResponse

class MultiCallRepository(private val api: Api) : ApiCallerInterface by ApiCaller() {
    suspend fun callAllMethods(): List<RequestResult<GreetingsResponse>> {
        return multiCall(api.postmanEcho(), api.postmanEcho(), api.postmanEchoNamed("Hello Carol!"))
    }

    suspend fun callTwoMethods(): List<RequestResult<GreetingsResponse>> =
        zip(api.greetings(), api.postmanEcho()) { res1, res2 ->
            listOf(res1, res2)
        }

    suspend fun callThreeMethods(): List<RequestResult<GreetingsResponse>> =
        zip(api.greetings(), api.postmanEcho(), api.postmanEchoNamed("Sarah")) { res1, res2, res3 ->
            listOf(res1, res2, res3)
        }

    suspend fun callArrayOfMethods(): List<RequestResult<GreetingsResponse>> =
        zipArray(api.postmanEcho(), api.postmanEchoNamed("Sarah")) { it }
}