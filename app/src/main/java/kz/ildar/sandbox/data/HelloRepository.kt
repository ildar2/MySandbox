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
import kz.ildar.sandbox.data.go.request
import kz.ildar.sandbox.data.model.GreetingWrapper
import kz.ildar.sandbox.data.model.GreetingsResponse

interface HelloRepository {
    fun giveHello(): String
    fun getImageUrl(): String
    suspend fun greetings(): RequestResult<GreetingsResponse>
    suspend fun personalGreeting(name: String): RequestResult<GreetingsResponse>
    suspend fun echoGreetings(): RequestResult<SafeResponse<GreetingsResponse>>
    suspend fun echoPersonalGreeting(name: String): RequestResult<SafeResponse<GreetingsResponse>>
    suspend fun echoGreetingsGo(): GreetingWrapper
    suspend fun echoPersonalGreetingGo(name: String): GreetingWrapper
}

class HelloRepositoryImpl(
    private val api: Api
) : HelloRepository, CoroutineCaller by ApiCaller {
    override fun getImageUrl() =
        "http://n1s2.hsmedia.ru/25/a6/f1/25a6f18bc39a1bb25576afcaf51b2b9e/440x326_21_f8bd2412cc3b290d7b5d30c1bc75c6ea@690x460_0xc0a8392b_17283489891476094168.jpeg"

    override fun giveHello() = "Hello Koin"

    override suspend fun greetings() = apiCall {
        api.greetings()
    }

    override suspend fun personalGreeting(name: String) = apiCall {
        api.personalGreetings(name)
    }

    override suspend fun echoGreetings() =
        api.postmanEchoSafe().call()

    override suspend fun echoPersonalGreeting(name: String) =
        api.postmanEchoNamedSafe("Hello, $name!").call()

    override suspend fun echoGreetingsGo() =
        api.postmanEchoGo().request()

    override suspend fun echoPersonalGreetingGo(name: String) =
        api.postmanEchoNamedGo("Hello, $name!").singleRequest().dto
}