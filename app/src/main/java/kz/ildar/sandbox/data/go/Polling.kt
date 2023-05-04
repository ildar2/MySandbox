package kz.ildar.sandbox.data.go

class Polling<T : Any>(
    val dto: T,
    val pollingMs: Long?
)

const val HEADER_POLLING_INTERVAL_MS = "Header-polling"

internal fun <T : Any> extractPolling(response: GoApiResponse<T>): Polling<T> {
    return Polling(dto = response.dto, pollingMs = parsePollingMs(response.headers))
}

private fun parsePollingMs(headers: Headers): Long? = with(headers) {
    extractPolling(header(HEADER_POLLING_INTERVAL_MS))
}

private fun extractPolling(raw: String?): Long? = raw?.toLongOrNull()?.takeIf { it > 0 }