package kz.ildar.sandbox.data.go.statebar

import kz.ildar.sandbox.R
import kz.ildar.sandbox.utils.IdResourceString

interface TaxiOrderStateProvider {
  fun isOrderSearching(): Boolean
}

class TaxiOrderStrategy(
    private val provider: TaxiOrderStateProvider
) : ShowStrategy {

  override fun shouldShowNoNetwork(screen: Screen): Boolean {
    return true
  }

  override fun customNoNetworkInfo(screen: Screen): NetworkStateRepository.NoNetworkInfo? {
    return if (provider.isOrderSearching()) {
      NetworkStateRepository.NoNetworkInfo(title = IdResourceString(R.string.toast_connection_lost_taxisearch))
    } else {
      null
    }
  }
}