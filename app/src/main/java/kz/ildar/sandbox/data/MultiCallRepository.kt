package kz.ildar.sandbox.data

import kz.ildar.sandbox.data.api.Api
import kz.ildar.sandbox.data.model.GreetingWrapper
import kz.ildar.sandbox.data.model.GreetingsResponse

class MultiCallRepository(private val api: Api) : ApiCallerInterface by ApiCaller() {
    suspend fun callAllMethods(): List<RequestResult<GreetingWrapper>> {
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

    suspend fun callArrayOfMethods(): List<RequestResult<GreetingWrapper>> =
        zipArray(api.postmanEcho(), api.postmanEchoNamed("Sarah")) { it }
}