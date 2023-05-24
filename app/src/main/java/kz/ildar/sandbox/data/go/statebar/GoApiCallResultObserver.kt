package kz.ildar.sandbox.data.go.statebar

/**
 * Observer will be triggered on any network request. This way you can subscribe on any retry or polling attempts.
 * Errors will be wrapped to GoApiException, except cancellation errors.
 * @see GoApiException
 */
interface GoApiCallResultObserver {

  fun onResult(callResult: CallResult)

  sealed class CallResult(val method: String) {
    class SuccessResult(method: String) : CallResult(method)
    class FailureResult(method: String, val error: Throwable) : CallResult(method)
  }

  companion object {
    val EMPTY = object : GoApiCallResultObserver {
      override fun onResult(callResult: CallResult) {}
    }
  }
}

