package retrofit2

fun <T> Call<T>.extractBody(rawResponse: okhttp3.Response): Response<T> {
    if (this is OkHttpCall) {
        return this.parseResponse(rawResponse)
    }
    throw IllegalStateException("Try to parse body not form retrofit call")
}