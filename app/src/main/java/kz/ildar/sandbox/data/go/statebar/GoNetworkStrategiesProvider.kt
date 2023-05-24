package kz.ildar.sandbox.data.go.statebar

class GoNetworkStrategiesProvider (
    taxiStateProvider: TaxiOrderStateProvider,
    shuttleStateProvider: ShuttleOrderStateProvider
): StrategiesProvider {

  override val strategies: Map<EndpointName, ShowStrategy> = mapOf(
      "persuggest/v1/finalsuggest" to DEFAULT_STRATEGY,
      "lbs" to DEFAULT_STRATEGY,
      "nearestzone" to DEFAULT_STRATEGY,
      "zoneinfo" to DEFAULT_STRATEGY,
      "taxiontheway" to TaxiOrderStrategy(taxiStateProvider),
      "shuttle-control/v1/booking/information" to ShuttleOrderStateStrategy(shuttleStateProvider),
      "userplaces" to DEFAULT_STRATEGY,
      "userplacesupdate" to DEFAULT_STRATEGY,
      "userplacenew" to DEFAULT_STRATEGY
  )

  companion object {
    private val DEFAULT_STRATEGY = DefaultBarShowStrategy()
  }
}