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