package kz.ildar.sandbox.data

import kz.ildar.sandbox.data.api.Api

class MultiCallRepository(private val api: Api) : MultiCoroutineCaller by ApiCaller() {
    suspend fun callTwoMethods() : List<RequestResult<*>> {
        return multiCall(api.greetings(), api.greetings())
    }
}