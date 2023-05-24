package kz.ildar.sandbox.data.go.statebar

import kz.ildar.sandbox.R
import kz.ildar.sandbox.data.go.statebar.NetworkStateRepository.NoNetworkInfo
import kz.ildar.sandbox.utils.IdResourceString

interface ShuttleOrderStateProvider {
  fun isOrderWaitingPayments(): Boolean
}

class ShuttleOrderStateStrategy(
    private val orderStateProvider: ShuttleOrderStateProvider
) : ShowStrategy {
  override fun shouldShowNoNetwork(screen: Screen): Boolean {
    return true
  }

  override fun customNoNetworkInfo(screen: Screen): NoNetworkInfo? {
    return if (orderStateProvider.isOrderWaitingPayments()) {
      NoNetworkInfo(
          title = IdResourceString(R.string.shuttle_payment_internet_error_title),
          subtitle = IdResourceString(R.string.shuttle_payment_internet_error_subtitle)
      )
    } else {
      null
    }
  }
}