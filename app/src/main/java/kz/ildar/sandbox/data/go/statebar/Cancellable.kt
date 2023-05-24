package kz.ildar.sandbox.data.go.statebar

/**
 * Token representing a cancellable operation.
 */
interface Cancellable {
    /**
     * Cancel the subscription. This call should be idempotent, making it safe to
     * call multiple times.
     */
    fun cancel()

    companion object {
        @JvmField
        val EMPTY: Cancellable = object : Cancellable {
            override fun cancel() = Unit
        }
    }
}