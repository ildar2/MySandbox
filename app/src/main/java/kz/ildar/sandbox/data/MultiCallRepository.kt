package kz.ildar.sandbox.data

import kz.ildar.sandbox.data.api.Api
import kz.ildar.sandbox.data.model.GreetingWrapper

class MultiCallRepository(private val api: Api) : MultiCoroutineCaller by ApiCaller() {
    suspend fun callAllMethods(): List<RequestResult<GreetingWrapper>> {
        return multiCall(api.postmanEcho(), api.postmanEcho(), api.postmanEchoNamed("Hello Carol!"))
    }
}