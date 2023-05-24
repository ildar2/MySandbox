package kz.ildar.sandbox.data.go.statebar

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kz.ildar.sandbox.utils.ResourceString

interface NetworkStateRepository {
    fun provideStates(): Flow<State>

    sealed class State {
        object Normal : State()
        class NoInternet(val info: NoNetworkInfo?) : State()
    }

    data class NoNetworkInfo(val title: ResourceString, val subtitle: ResourceString? = null)

    companion object {

        val EMPTY = object : NetworkStateRepository {
            override fun provideStates(): Flow<State> = emptyFlow()
        }
    }
}
