package kz.ildar.sandbox.data.go

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import retrofit2.HttpException
import retrofit2.extractBody
import java.io.IOException
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import retrofit2.Call as RCall

class GoApiCallImpl<T : Any>(
    private val callFactory: Call.Factory,
    private val originalCall: RCall<T>
) : GoApiCall<T> {

    override suspend fun singleRequest(): GoApiResponse<T> {
        return try {
            makeRequest(createCallRequest())
        } catch (ex: Throwable) {
            throw when (ex) {
                is CancellationException -> ex
                is HttpException -> GoApiHttpException(
                    code = ex.code(),
                    headers = Headers(ex.response()?.headers()?.toMultimap() ?: emptyMap())
                )
                else -> GoApiOtherException(original = ex)
            }
        }
    }

    private suspend fun createCallRequest(): Request {
        val modifier = currentCoroutineContext()[RequestModifier]
        val headers = modifier?.additionalHeaders ?: emptyMap()
        val request = originalCall.request()
        if (headers.isEmpty()) {
            return request
        }
        return request.newBuilder()
            .apply {
                for ((key, value) in headers) {
                    header(key, value)
                }
            }
            .build()
    }

    private suspend fun makeRequest(request: Request): GoApiResponse<T> {
        return suspendCancellableCoroutine { continuation ->
            val call = callFactory.newCall(request)
            continuation.invokeOnCancellation { call.cancel() }

            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    continuation.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: okhttp3.Response) {
                    val retrofitResponse = try {
                        originalCall.extractBody(response)
                    } catch (e: Throwable) {
                        continuation.resumeWithException(e)
                        return
                    }

                    val body = retrofitResponse.body()
                    if (retrofitResponse.isSuccessful && body != null) {
                        continuation.resume(
                            GoApiResponse(
                                dto = body,
                                code = retrofitResponse.code(),
                                headers = Headers(
                                    rawHeaders = retrofitResponse.headers().toMultimap()
                                )
                            )
                        )
                    } else {
                        continuation.resumeWithException(HttpException(retrofitResponse))
                    }
                }
            })
        }
    }
}

internal data class RequestModifier(
    val additionalHeaders: Map<String, String>
) : AbstractCoroutineContextElement(RequestModifier) {
    companion object Key : CoroutineContext.Key<RequestModifier>

    override fun toString(): String = "RequestModifier($additionalHeaders)"
}