package kz.ildar.sandbox.data.go.statebar

import java.io.IOException
import java.util.concurrent.atomic.AtomicReference
import javax.net.ssl.SSLException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kz.ildar.sandbox.data.go.GoApiOtherException
import kz.ildar.sandbox.data.go.statebar.NetworkStateRepository.NoNetworkInfo
import kz.ildar.sandbox.data.go.statebar.NetworkStateRepository.State
import kz.ildar.sandbox.data.go.statebar.NetworkStateRepository.State.NoInternet
import kz.ildar.sandbox.data.go.statebar.NetworkStateRepository.State.Normal

typealias EndpointName = String

interface ShowStrategy {
  fun shouldShowNoNetwork(screen: Screen): Boolean
  fun customNoNetworkInfo(screen: Screen): NoNetworkInfo?
}

interface StrategiesProvider {
  val strategies: Map<EndpointName, ShowStrategy>
}

/**
 * Observer of any network requests and shows "No network" info to user when it is necessary.
 */
class StateBarGoApiCallResultObserver (
  currentScreenRepository: CurrentScreenRepository,
  private val strategiesProvider: StrategiesProvider
) : GoApiCallResultObserver, CurrentScreenRepository.Listener, NetworkStateRepository {

  private val currentScreen = AtomicReference<Screen>()
  private val strategies get() = strategiesProvider.strategies
  private val stateFlow: MutableStateFlow<State> = MutableStateFlow(Normal)

  init {
    currentScreenRepository.addListener(this)
  }

  override fun onScreenChanged(screen: Screen) {
    currentScreen.set(screen)
    backToNormal()
  }

  override fun onResult(callResult: GoApiCallResultObserver.CallResult) {
    val strategy = strategies[callResult.method]
    val screen = currentScreen.get() ?: return

    when(callResult) {
      is GoApiCallResultObserver.CallResult.FailureResult -> {
        strategy ?: return
        if (callResult.error.isNoConnectionError() && strategy.shouldShowNoNetwork(screen)) {
            noInternet(strategy.customNoNetworkInfo(screen))
        }
      }
      is GoApiCallResultObserver.CallResult.SuccessResult -> {
        backToNormal()
      }
    }
  }

  private fun noInternet(noNetworkInfo: NoNetworkInfo?) {
    stateFlow.value = NoInternet(noNetworkInfo)
  }

  private fun backToNormal() {
    stateFlow.value = Normal
  }

  override fun provideStates(): Flow<State> {
    return stateFlow
  }

  private fun Throwable.isNoConnectionError(): Boolean {
    return if (this is GoApiOtherException) {
      isNetworkError(original)
    } else {
      isNetworkError(this)
    }
  }


  private fun isNetworkError(e: Throwable?): Boolean {
    return e is IOException && e !is SSLException
  }
}