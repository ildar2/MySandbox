package kz.ildar.sandbox.data

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlin.coroutines.resumeWithException

/**
 * Should be used as a wrapper for Retrofit api calls
 *
 * [singleRequest] for requests without retry and error handling (internal use)
 *
 * use [call] for api calls with retry - preferred
 *
 * use [callStrict] for api calls with retry and exception throwing
 *
 * Usage:
 *
 * &#64;GET
 * fun someApi(@Query String param): GoApiCall&lt;ResponseDto&gt;
 *
 */
interface SafeApiCall<T : Any> {
    suspend fun singleRequest(attempt: Int?, lastCode: Int?): SafeResponse<T>
}

/**
 * DTO wrapper for api calls with [SafeApiCall]
 */
data class SafeResponse<out T>(
    val dto: T,
    val code: Int = 0,
    val headers: Map<String, String> = emptyMap(),
)

class SafeApiCallImpl<R : Any>(private val call: Call<R>) : SafeApiCall<R> {
    override suspend fun singleRequest(attempt: Int?, lastCode: Int?): SafeResponse<R> =
        suspendCancellableCoroutine { continuation ->
            val newCall = call.clone()
            val newRequest = newCall.request().newBuilder()
                .apply {
                    attempt?.let { header("Header-Retry-Number", attempt.toString()) }
                    lastCode?.let { header("Header-Last-Code", lastCode.toString()) }
                }
                .build()

            val rawCall = newCall.javaClass.getDeclaredField("rawCall")
                .also { it.isAccessible = true }.get(newCall)
            rawCall.javaClass.getDeclaredField("originalRequest")
                .also { it.isAccessible = true }.set(rawCall, newRequest)

            newCall.enqueue(object : Callback<R> {
                override fun onResponse(call: Call<R>, response: Response<R>) {
                    if (response.isSuccessful) {
                        continuation.resumeWith(Result.success(SafeResponse(
                            dto = response.body()!!,
                            response.code(),
                            response.headers().toMap()
                        )))
                    } else {
                        continuation.resumeWithException(
                            HttpException(response)
                        )
                    }
                }

                override fun onFailure(call: Call<R>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
            continuation.invokeOnCancellation { call.cancel() }
        }
}

class GoApiCallAdapterFactory : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {

        // check for suspend
        if (
            Call::class.java == getRawType(returnType)
            && returnType is ParameterizedType
            && getRawType(getParameterUpperBound(0, returnType)) == SafeApiCall::class.java
        ) {
            throw IllegalStateException("Don't use suspend with GoApiCall")
        }

        // target check
        if (getRawType(returnType) != SafeApiCall::class.java) {
            return null
        }

        check(returnType is ParameterizedType) {
            "Response must be parameterized as SafeApiCall<Foo> or SafeApiCall<out Foo>"
        }

        val successBodyType = getParameterUpperBound(0, returnType)

        return object : CallAdapter<Any, SafeApiCall<Any>> {
            override fun responseType(): Type = successBodyType

            override fun adapt(call: Call<Any>): SafeApiCall<Any> = SafeApiCallImpl(call)
        }
    }
}

/**
 * Handle all types of errors and retry if error is retryable
 * @see ApiCaller.handleException
 * @see RetryAction
 * @param onThrowable - called with [RequestError] each time api call is retried, so we can cancel it
 * @return dto wrapped in [SafeResponse] and [RequestResult]
 */
suspend fun <T : Any> SafeApiCall<T>.call(
    onThrowable: ((Throwable) -> Unit)? = null
): RequestResult<SafeResponse<T>> = callInternal(onThrowable) { retryNumber, lastStatusCode ->
    singleRequest(retryNumber, lastStatusCode)
}

/**
 * Handle all types of errors and retry if error is retryable
 * @see ApiCaller.handleException
 * @see RetryAction
 * @param onThrowable - called with [RequestError] each time api call is retried, so we can cancel it
 * @throws RequestError
 */
suspend fun <T : Any> SafeApiCall<T>.callStrict(
    onThrowable: ((Throwable) -> Unit)? = null
): SafeResponse<T> = when (val result = callInternal(onThrowable) { retryNumber, lastStatusCode ->
    singleRequest(retryNumber, lastStatusCode)
}) {
    is RequestResult.Success -> result.result
    is RequestResult.Error -> throw RequestError(result.error, result.code)
}

private suspend fun <T> callInternal(
    onThrowable: ((Throwable) -> Unit)? = null,
    attempt: Int? = null,
    lastErrorCode: Int? = null,
    request: suspend (retryNumber: Int?, lastStatusCode: Int?) -> T
): RequestResult<T> = try {
    coroutineScope {
        val response = request.invoke(attempt, lastErrorCode)
        RequestResult.Success(response)
    }
} catch (e: Throwable) {
    val error = ApiCaller.handleException(e, null, attempt ?: 0)
    onThrowable?.invoke(RequestError(error.error, error.code))
    if (error.retryAction != null && error.retryAction != RetryAction.Stop) {
        delay(error.retryAction.retryDelayMs())
        callInternal(
            onThrowable,
            (attempt ?: 0) + 1,
            error.code,
            request,
        )
    } else {
        error
    }
}
