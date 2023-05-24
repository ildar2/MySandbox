package kz.ildar.sandbox.data.go.statebar

import retrofit2.safeCollectIn

class InternetConnectionStateBarMediator (
  private val repository: NetworkStateRepository
) {

  private var controller: InternetConnectionStateBarController? = null
  private var scope = ScopeDelegate.main()

  fun attach(controller: InternetConnectionStateBarController?) {
    this.controller = controller
    scope.attach()
    repository.provideStates()
      .safeCollectIn(scope.scope) { state ->
        when(state) {
          is NetworkStateRepository.State.NoInternet -> showNoInternet(state.info)
          NetworkStateRepository.State.Normal -> hide()
        }
      }
  }

  fun detach() {
    this.controller = null
    scope.detach()
  }

  private fun showNoInternet(noNetworkInfo: NetworkStateRepository.NoNetworkInfo?) {
    val controller = controller ?: return
    if (controller.isStateBarVisible) return
    if (noNetworkInfo == null) {
      controller.setNoInternetTitles()
    } else {
      controller.setTitle(noNetworkInfo.title)
      controller.setInfo(noNetworkInfo.subtitle)
    }
    controller.setConnectionState(false)
    controller.invalidate()
  }

  private fun hide() {
    val controller = controller ?: return
    if (!controller.isStateBarVisible) return
    controller.hide()
  }

}