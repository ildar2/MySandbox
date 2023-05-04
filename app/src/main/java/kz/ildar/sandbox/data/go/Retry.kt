package kz.ildar.sandbox.data.go

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import kotlin.math.pow

const val BASE_SEED = 500L
const val DEFAULT_MAX_CLIENT_RETRIES = 3

@Suppress("NOTHING_TO_INLINE")
internal suspend inline fun <T : Any> GoApiCall<T>.makeRequestWithRetry(
    noinline onError: ((Throwable) -> Unit)? = null,
    maxRetries: Int = DEFAULT_MAX_CLIENT_RETRIES
): GoApiResponse<T> {
    var retryNumber = 0
    var lastStatusCode = 0

    while (true) {
        currentCoroutineContext().ensureActive()
        try {
            return withContext(requestModifier(retryNumber, lastStatusCode)) {
                singleRequest()
            }
        } catch (ex: GoApiException) {
            onError?.invoke(ex)

            if (ex is GoApiHttpException) {
                lastStatusCode = ex.code
            }

            when (val retryAction =
                extractRetryAction(retryNumber = retryNumber++, ex = ex, maxRetries = maxRetries)) {
                RetryAction.Stop -> throw ex
                is RetryAction.FixedDelay -> delay(retryAction.delayMs)
            }
        }
    }
}

private fun requestModifier(
    retryNumber: Int,
    lastStatusCode: Int
) = RequestModifier(
    additionalHeaders = when {
        retryNumber > 0 -> mapOf(
            "Header-Retry-Number" to "$retryNumber",
            "Header-Last-Code" to "$lastStatusCode",
        )
        else -> emptyMap()
    }
)

private fun extractRetryAction(retryNumber: Int, ex: GoApiException, maxRetries: Int): RetryAction {
    val result = when (ex) {
        is GoApiHttpException -> when {
            isRetryableError(ex.code) -> safeParseRetryAction(ex.headers)
            else -> RetryAction.Stop
        }
        is GoApiOtherException -> null
    }
    return result ?: defaultRetryInterval(attempt = retryNumber, maxClientRetries = maxRetries)
}

private fun isRetryableError(code: Int): Boolean {
    return code == 429 || code == 404
}

private fun defaultRetryInterval(attempt: Int, maxClientRetries: Int): RetryAction =
    when (attempt) {
        in 0 until maxClientRetries -> RetryAction.FixedDelay(BASE_SEED * 2.0.pow(attempt).toLong())
        else -> RetryAction.Stop
    }