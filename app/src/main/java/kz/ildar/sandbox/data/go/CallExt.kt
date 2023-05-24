package retrofit2

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import timber.log.Timber

fun <T> Call<T>.extractBody(rawResponse: okhttp3.Response): Response<T> {
    if (this is OkHttpCall) {
        return this.parseResponse(rawResponse)
    }
    throw IllegalStateException("Try to parse body not form retrofit call")
}

inline fun <T> Flow<T>.safeCollectIn(
    scope: CoroutineScope,
    crossinline action: suspend (value: T) -> Unit,
    crossinline onError: suspend (value: Throwable) -> Unit
): Job = scope.launch {
    safeCollect(action, onError)
}

inline fun <T> Flow<T>.safeCollectIn(
    scope: CoroutineScope,
    crossinline action: suspend (value: T) -> Unit
): Job = scope.launch {
    safeCollect(action, onUnexpectedError())
}

/**
 * onError can catch CancellationExceptions if upstream threw it
 * because of timeout or something else. In this case cancellation should be
 * handled as usual exception
 */
suspend inline fun <T> Flow<T>.safeCollect(
    crossinline action: suspend (value: T) -> Unit,
    crossinline onError: suspend (value: Throwable) -> Unit
) {
    this
        .catch { cause ->
            onError(cause)
        }
        .collect { action(it) }
}

fun onUnexpectedError(): suspend (value: Throwable) -> Unit {
    val exception = IllegalStateException("No exception handlers defined")
    return { throwable: Throwable? ->
        exception.initCause(throwable)
        Timber.wtf(exception, "Unexpected exception thrown")
    }
}