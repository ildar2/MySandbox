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

import kotlinx.coroutines.Deferred
import kz.ildar.sandbox.data.model.Greeting
import kz.ildar.sandbox.data.model.GreetingWrapper
import kz.ildar.sandbox.data.model.GreetingsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("/greeting")
    fun greetings(): Deferred<Response<Greeting>>

    @GET("/greeting")
    fun personalGreetings(@Query("name") name: String): Deferred<Response<Greeting>>


    @GET("/get?content=Hello")
    fun postmanEcho(): Deferred<Response<GreetingWrapper>>

    @GET("/get")
    fun postmanEchoNamed(@Query("content") name: String): Deferred<Response<GreetingWrapper>>


    @GET("/get?content=Hello")
    fun postmanEchoOld(): Deferred<Response<GreetingWrapper>>

    @GET("/get")
    fun postmanEchoNamedOld(@Query("content") name: String): Deferred<Response<GreetingWrapper>>
}