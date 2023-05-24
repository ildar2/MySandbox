package kz.ildar.sandbox.data.go.statebar

import android.annotation.SuppressLint
import android.graphics.Color
import kz.ildar.sandbox.R
import kz.ildar.sandbox.utils.IdResourceString
import kz.ildar.sandbox.utils.ResourceString

class InternetConnectionStateBarController(
    currentScreenRepository: CurrentScreenRepository,
    stateBarVisibilityTracker: StateBarVisibilityTracker
) : StateBarController(currentScreenRepository, stateBarVisibilityTracker) {
    private var connected = true
    private var titleText: ResourceString? = null
    private var infoText: ResourceString? = null
    public override fun start(viewHolder: StateBarViewHolder) {
        super.start(viewHolder)
    }

    public override fun stop() {
        super.stop()
    }

    fun setConnectionState(connected: Boolean): InternetConnectionStateBarController {
        this.connected = connected
        return this
    }

    fun setNoInternetTitles(): InternetConnectionStateBarController {
        titleText = IdResourceString(R.string.request_connection_error)
        infoText = IdResourceString(R.string.request_connection_error)
        return this
    }

    fun setTitle(title: ResourceString?): InternetConnectionStateBarController {
        titleText = title
        return this
    }

    fun setInfo(info: ResourceString?): InternetConnectionStateBarController {
        infoText = info
        return this
    }

    fun invalidate() {
        onChanged()
    }

    override val isStateBarVisible: Boolean
        get() = !connected

    public override fun onChanged() {
        val stateBarHolder = stateBarHolder ?: return
        stateBarHolder.setState(provideModel())
        onVisibilityChanged(isStateBarVisible)
    }

    @SuppressLint("NewApi")
    private fun provideModel(): StateBarViewModel {
        val textColor = Color.valueOf(Color.WHITE)
        return StateBarViewModel.Builder(StateBarViewModel.Type.NO_INTERNET_CONNECTION)
            .withVisibility(isStateBarVisible)
            .withTitle(if (titleText != null) titleText else IdResourceString(R.string.request_connection_error))
            .withSubtitle(infoText)
            .withTitleColor(textColor)
            .withSubtitleColor(textColor)
            .withBackgroundColor(Color.valueOf(Color.RED))
            .build()
    }

    fun hide() {
        connected = true
        invalidate()
    }
}