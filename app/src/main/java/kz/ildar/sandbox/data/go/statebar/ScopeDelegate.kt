package kz.ildar.sandbox.data.go.statebar

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import timber.log.Timber
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Holds logic for scope creation
 * Basically valid scope created only when call attach()
 * when call detach() full scope would be cancelled
 * after scope detach() new coroutines would not start
 * */
class ScopeDelegate(
    private val dispatcherProvider: () -> CoroutineDispatcher,
    private val scopeName: String = "",
    private val exceptionHandler: CoroutineExceptionHandler? = null
) {
  private var innerScope: CoroutineScope? = null

  /**
   * Return scope for coroutines. If scope does not attach, return cancelled scope that does not run new coroutines
   * */
  val scope: CoroutineScope
    get() = innerScope ?: cancelledScope("mainScope")

  val isActive: Boolean
    get() = innerScope?.isActive == true

  /**
   * Prepare scope to be available launch new coroutines
   * */
  fun attach() {
    var newInnerScope = CoroutineScope(SupervisorJob() + dispatcherProvider()) + CoroutineName(scopeName)
    if (exceptionHandler != null) {
      newInnerScope += exceptionHandler
    }
    innerScope = newInnerScope
  }

  /**
   * Cancel all run coroutines in scope
   * */
  fun detach() {
    innerScope?.cancel("detach scope")
  }

  /**
   * Cancel all run coroutines in scope and attaches again
   * */
  fun reattach() {
    detach()
    attach()
  }

  fun launch(
      context: CoroutineContext = EmptyCoroutineContext,
      start: CoroutineStart = CoroutineStart.DEFAULT,
      block: suspend CoroutineScope.() -> Unit
  ): Job {
    return scope.launch(context, start, block)
  }

  internal fun cancelledScope(scopeName: String): CoroutineScope {
    val exception = IllegalStateException("Using cancelled scope instead of $scopeName")
    Timber.e(exception)

    val scope = MainScope()
    scope.cancel("Already cancelled", exception)
    return scope
  }

  companion object {
    /**
     * Return Scope Delegate that run coroutines on main thread
     * */
    @JvmStatic
    @JvmOverloads
    fun main(scopeName: String? = null, exceptionHandler: CoroutineExceptionHandler? = null): ScopeDelegate {
      return ScopeDelegate({ Dispatchers.Main }, scopeName ?: "", exceptionHandler)
    }
  }
}
