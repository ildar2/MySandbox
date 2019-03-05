package kz.ildar.sandbox.data

import kz.ildar.sandbox.data.api.Api
import kz.ildar.sandbox.data.model.Greeting

interface HelloRepository {
    fun giveHello(): String
    fun getImageUrl(): String
    suspend fun greetings(): RequestResult<Greeting>
    suspend fun personalGreeting(name: String): RequestResult<Greeting>
}

class HelloRepositoryImpl(val api: Api) : HelloRepository, CoroutineCaller by BaseRepository() {
    override fun getImageUrl() =
        "http://n1s2.hsmedia.ru/25/a6/f1/25a6f18bc39a1bb25576afcaf51b2b9e/440x326_21_f8bd2412cc3b290d7b5d30c1bc75c6ea@690x460_0xc0a8392b_17283489891476094168.jpeg"

    override fun giveHello() = "Hello Koin"

    override suspend fun greetings() = coroutineApiCall(api.greetings())

    override suspend fun personalGreeting(name: String) = coroutineApiCall(api.personalGreetings(name))
}