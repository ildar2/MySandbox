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

    suspend fun <T1, T2, R> zip(
        request1: Deferred<Response<T1>>,
        request2: Deferred<Response<T2>>,
        zipper: (RequestResult<T1>, RequestResult<T2>) -> R
    ): R = zipper(coroutineApiCall(request1), coroutineApiCall(request2))

    suspend fun <T1, T2, T3, R> zip(
        request1: Deferred<Response<T1>>,
        request2: Deferred<Response<T2>>,
        request3: Deferred<Response<T3>>,
        zipper: (RequestResult<T1>, RequestResult<T2>, RequestResult<T3>) -> R
    ): R = zipper(coroutineApiCall(request1), coroutineApiCall(request2), coroutineApiCall(request3))

    suspend fun <T, R> zipArray(
        vararg requests: Deferred<Response<T>>,
        zipper: (List<RequestResult<T>>) -> R
    ): R = zipper(requests.map { coroutineApiCall(it) })

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