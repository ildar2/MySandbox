package kz.ildar.sandbox.data.go.statebar

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class StateBarVisibilityTracker {
    private val visibilityFlowInner = MutableStateFlow(false)
    private val visibilityProviders = hashSetOf<() -> Boolean>()

    val visibilityFlow: Flow<Boolean>
        get() = visibilityFlowInner

    val stateBarVisible: Boolean
        get() = visibilityProviders.any { it() }

    fun register(provider: () -> Boolean): Cancellable {
        visibilityProviders.add(provider)
        return object : Cancellable {
            override fun cancel() {
                visibilityProviders.remove(provider)
            }
        }
    }

    fun refreshVisibility() {
        visibilityFlowInner.value = stateBarVisible
    }
}