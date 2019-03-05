package kz.ildar.sandbox.data.api

import kotlinx.coroutines.Deferred
import kz.ildar.sandbox.data.model.Greeting
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("/greeting")
    fun greetings(): Deferred<Response<Greeting>>

    @GET("/greeting")
    fun personalGreetings(@Query("name") name: String): Deferred<Response<Greeting>>
}