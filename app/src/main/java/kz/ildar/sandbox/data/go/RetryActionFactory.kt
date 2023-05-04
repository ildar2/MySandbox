package kz.ildar.sandbox.data.go

import timber.log.Timber
import java.util.concurrent.TimeUnit.SECONDS

sealed interface RetryAction {
    object Stop : RetryAction

    class FixedDelay(val delayMs: Long) : RetryAction
}

private const val DATE_HEADER = "Date"
private const val STOP_ACTION = "stop"
private const val HEADER_TAXI_RETRY_ACTION = "Header-retry-action"
private const val HEADER_RETRY_AFTER = "Header-retry-after"
private const val HEADER_TAXI_RETRY_INTERVAL_MS = "Header-retry-interval"

fun safeParseRetryAction(headers: Headers): RetryAction? {
    return try {
        parseRetryAction(headers)
    } catch (ex: Throwable) {
        Timber.e(ex, "Invalid retry headers received")
        null
    }
}

fun parseRetryAction(headers: Headers): RetryAction? {
    return when (headers.header(HEADER_TAXI_RETRY_ACTION)) {
        null -> null
        STOP_ACTION -> RetryAction.Stop
        else -> parseRetryActionInterval(headers)
    }
}

private fun parseRetryActionInterval(headers: Headers): RetryAction? {
    val retryAfterSeconds = headers.header(HEADER_RETRY_AFTER)
    if (retryAfterSeconds != null) {
        val retryAt = parse(retryAfterSeconds)

        // retry at in date format
        if (retryAt != null) {
            val serverDateRaw = headers.header(DATE_HEADER) ?: ""
            val serverTime: Long = 0//HttpDate.parse(serverDateRaw)
//                ?: throw IllegalStateException("Invalid request date format. Service Date: $serverDateRaw, Retry date: $retryAfterSeconds")
            val delayMs = retryAt - serverTime
            if (delayMs < 0) {
                throw IllegalStateException("Invalid request date diff. Service Date: $serverDateRaw, Retry date: $retryAfterSeconds")
            }
            return RetryAction.FixedDelay(delayMs = delayMs)
        }

        // retry at in seconds format
        val retryMs = retryAfterSeconds.toLongOrNull()?.let(SECONDS::toMillis)
            ?: throw IllegalStateException("Invalid request format. Retry seconds delay: $retryAfterSeconds")

        return RetryAction.FixedDelay(delayMs = retryMs)
    }

    val retryInterval = headers.header(HEADER_TAXI_RETRY_INTERVAL_MS)?.toLongOrNull()
    if (retryInterval != null && retryInterval > 0) {
        return RetryAction.FixedDelay(delayMs = retryInterval)
    }

    return null
}

fun parse(retryAfterSeconds: String): Long? {
    return null
}
