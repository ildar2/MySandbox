package kz.ildar.sandbox.ui.main.child

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow

class ConnectivityInteractor(
    private val connectivityManager: ConnectivityManager?
) {

    val capabilities = MutableSharedFlow<IntArray>(0, 1, BufferOverflow.DROP_OLDEST)

    init {
        connectivityManager?.registerNetworkCallback(
            NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build(),
            object : ConnectivityManager.NetworkCallback() {
                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities,
                ) {
                    capabilities.tryEmit(networkCapabilities.capabilities)
                }
            })
    }

    fun getCurrentConnection(): IntArray {
        connectivityManager ?: return IntArray(0)
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager
            .getNetworkCapabilities(network)
        return capabilities?.capabilities ?: IntArray(0)
    }

    private fun hasInternetConnection(): Boolean {
        connectivityManager ?: return false
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities != null
            && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
}