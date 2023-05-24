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
import kz.ildar.sandbox.data.go.statebar.GoApiCallResultObserver
import retrofit2.Call as RCall

class GoApiCallImpl<T : Any>(
    private val callFactory: Call.Factory,
    private val originalCall: RCall<T>,
    private val observer: GoApiCallResultObserver
) : GoApiCall<T> {

    override suspend fun singleRequest(): GoApiResponse<T> {
        return try {
            val result = makeRequest(createCallRequest())
            observer.onResult(successResult())
            result
        } catch (ex: Throwable) {
            val error = when (ex) {
                is CancellationException -> ex
                is HttpException -> GoApiHttpException(
                    code = ex.code(),
                    headers = Headers(ex.response()?.headers()?.toMultimap() ?: emptyMap())
                )
                else -> GoApiOtherException(original = ex)
            }
            observer.onResult(errorResult(error))
            throw error
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

    private fun successResult(): GoApiCallResultObserver.CallResult.SuccessResult {
        return GoApiCallResultObserver.CallResult.SuccessResult(originalCall.request().url.encodedPath.cutApiVersion())
    }

    private fun errorResult(error: Throwable): GoApiCallResultObserver.CallResult.FailureResult {
        return GoApiCallResultObserver.CallResult.FailureResult(
            originalCall.request().url.encodedPath.cutApiVersion(),
            error
        )
    }

    private fun String.cutApiVersion(): String {
        if (this.isBlank()) return this
        val prettied = trim()
        return if (prettied[0] == '/') {
            prettied.removeRange(0, 1).substringAfter("/")
        } else {
            prettied.substringAfter("/")
        }
    }
}

internal data class RequestModifier(
    val additionalHeaders: Map<String, String>
) : AbstractCoroutineContextElement(RequestModifier) {
    companion object Key : CoroutineContext.Key<RequestModifier>

    override fun toString(): String = "RequestModifier($additionalHeaders)"
}