package kz.ildar.sandbox.data

import kz.ildar.sandbox.data.api.Api

class MultiCallRepository(private val api: Api) : MultiCoroutineCaller by ApiCaller() {
    suspend fun callTwoMethods() : RequestResult<List<RequestResult<*>>> {
        return multiCall(api.postmanEcho(), api.postmanEcho())
    }
}