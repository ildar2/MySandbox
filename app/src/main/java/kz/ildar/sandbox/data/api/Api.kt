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
package kz.ildar.sandbox.data.api

import kz.ildar.sandbox.data.SafeApiCall
import kz.ildar.sandbox.data.go.GoApiCall
import kz.ildar.sandbox.data.model.Greeting
import kz.ildar.sandbox.data.model.GreetingWrapper
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface Api {
    @GET("/greeting")
    suspend fun greetings(): Greeting

    @GET("/greeting")
    suspend fun personalGreetings(@Query("name") name: String): Greeting

    @GET("/get?content=Hello")
    suspend fun postmanEcho(): GreetingWrapper

    @GET("/get")
    suspend fun postmanEchoNamed(@Query("content") name: String): GreetingWrapper

    @GET("/get?content=Hello")
    fun postmanEchoSafe(): SafeApiCall<GreetingWrapper>

    @GET("/get")
    fun postmanEchoNamedSafe(@Query("content") name: String): SafeApiCall<GreetingWrapper>

    @GET("/get?content=Hello")
    fun postmanEchoGo(): GoApiCall<GreetingWrapper>

    @GET("/get")
    fun postmanEchoNamedGo(@Query("content") name: String): GoApiCall<GreetingWrapper>
}