package kz.ildar.sandbox.data

import kotlinx.coroutines.Deferred
import kz.ildar.sandbox.data.api.Api
import kz.ildar.sandbox.data.model.GreetingWrapper
import kz.ildar.sandbox.data.model.GreetingsResponse
import retrofit2.Response

class MultiCallRepository(private val api: Api) : ApiCallerInterface by ApiCaller() {
    suspend fun callAllMethods(): List<RequestResult<GreetingWrapper>> {
        return multiCall(api.postmanEcho(), api.postmanEcho(), api.postmanEchoNamed("Hello Carol!"))
    }

    suspend fun <T1 : Any, T2 : Any, R> zip2(
        deferred1: Deferred<Response<T1>>,
        deferred2: Deferred<Response<T2>>,
        zipFunc: (RequestResult<T1>, RequestResult<T2>) -> R
    ): R {
        return zipFunc(coroutineApiCall(deferred1), coroutineApiCall(deferred2))
    }

    suspend fun callTwoMethods(): List<RequestResult<GreetingsResponse>> {
        val ans = ArrayList<RequestResult<GreetingsResponse>>()
        return zip2(api.greetings(), api.postmanEcho()) { res1, res2 ->
            ans.add(res1)
            ans.add(res2)
            ans
        }
    }

}