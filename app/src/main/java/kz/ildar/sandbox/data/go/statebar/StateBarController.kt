package kz.ildar.sandbox.data.go.statebar

import androidx.annotation.CallSuper

abstract class StateBarController protected constructor(
    private val currentScreenRepository: CurrentScreenRepository,
    private val stateBarVisibilityTracker: StateBarVisibilityTracker
) {
    private val stateBarVisibilityProvider = { isStateBarVisible }
    private val stateChangeListeners = mutableListOf<StateBarVisibilityListener>()
    private var visibilityProviderCancellable = Cancellable.EMPTY

    //  @NonNull
    //  private Subscription screenChangesSubscription = Subscriptions.empty();
    protected var stateBarHolder: StateBarViewHolder? = null
        private set

    @CallSuper
    protected open fun start(viewHolder: StateBarViewHolder) {
        stateBarHolder = viewHolder
        visibilityProviderCancellable =
            stateBarVisibilityTracker.register(stateBarVisibilityProvider)
        //    screenChangesSubscription = currentScreenRepository.addListener(currentScreen -> onChanged());
    }

    @CallSuper
    protected open fun stop() {
//    screenChangesSubscription.unsubscribe();
        visibilityProviderCancellable.cancel()
        stateBarHolder = null
    }

    fun addChangeStateListener(listener: StateBarVisibilityListener) {
        stateChangeListeners.add(listener)
    }

    protected abstract fun onChanged()
    protected fun onVisibilityChanged(isVisible: Boolean) {
        for (stateChangeListener in stateChangeListeners) {
            stateChangeListener.onVisibilityChanged(isVisible)
        }
        stateBarVisibilityTracker.refreshVisibility()
    }

    abstract val isStateBarVisible: Boolean
}