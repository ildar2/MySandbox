package kz.ildar.sandbox.data.go.statebar

class DefaultBarShowStrategy : ShowStrategy {

  override fun shouldShowNoNetwork(screen: Screen): Boolean {
    return true
  }

  override fun customNoNetworkInfo(screen: Screen): NetworkStateRepository.NoNetworkInfo? {
    return null
  }

}