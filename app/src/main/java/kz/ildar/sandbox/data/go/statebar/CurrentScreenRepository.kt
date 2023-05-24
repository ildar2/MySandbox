package kz.ildar.sandbox.data.go.statebar

import kz.ildar.sandbox.data.go.statebar.Screen.*

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.suspendCancellableCoroutine

class CurrentScreenRepository {

  private val listeners = mutableListOf<Listener>()

  private var currentScreen = NONE

  val isMain: Boolean
    get() = currentScreen == MAIN

  val isSummary: Boolean
    get() = currentScreen == SUMMARY

  val isOnOrder: Boolean
    get() = currentScreen == ORDER || currentScreen == ORDERS_LIST
        || currentScreen == ORDER_DETAILS || currentScreen == ORDER_FEED_DETAILS

  val isBundledOrderList: Boolean
    get() = currentScreen == BUNDLED_ORDER_LIST

  fun addListener(listener: Listener) {
    listeners.add(listener)
  }

  fun addOneShotListener(listener: Listener) {
    listeners.add(listener)
  }

  private fun addCancelableListener(listener: Listener) {
    listeners.add(listener)
  }

  suspend fun waitForScreenChange(): Screen {
    return suspendCancellableCoroutine { continuation ->
//      val subscription = listeners.addOneShot(continuation::resume)
//      continuation.invokeOnCancellation { subscription.unsubscribe() }
    }
  }

  val currentScreenFlow: Flow<Screen>? = null

  fun getCurrentScreen(): Screen = currentScreen

  fun setCurrentScreen(screen: Screen) {
    if (currentScreen != screen) {
      currentScreen = screen
      listeners.forEach { it.onScreenChanged(currentScreen) }
    }
  }

  fun interface Listener {
    fun onScreenChanged(screen: Screen)
  }

  interface TrackedScreen {
    val trackedScreen: Screen
  }
}