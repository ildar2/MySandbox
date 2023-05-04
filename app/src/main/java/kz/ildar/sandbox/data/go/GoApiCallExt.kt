package kz.ildar.sandbox.data.go

suspend fun <T : Any> GoApiCall<T>.singleRequestDto(): T {
    return singleRequest().dto
}

suspend fun <T : Any> GoApiCall<T>.requestFull(onError: ((Throwable) -> Unit)? = null): GoApiResponse<T> {
    return makeRequestWithRetry(onError = onError)
}

suspend fun <T : Any> GoApiCall<T>.request(onError: ((Throwable) -> Unit)? = null): T {
    return makeRequestWithRetry(onError = onError).dto
}

suspend fun <T : Any> GoApiCall<T>.singleRequestPolling(): Polling<T> =
    extractPolling(singleRequest())

suspend fun <T : Any> GoApiCall<T>.requestPolling(onError: ((Throwable) -> Unit)? = null): Polling<T> {
    return extractPolling(requestFull(onError = onError))
}