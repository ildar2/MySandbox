package kz.ildar.sandbox.data.go.statebar

import android.graphics.Color
import kz.ildar.sandbox.utils.ResourceString

class StateBarViewModel(builder: Builder) {
    val isVisible: Boolean
    val isProgress: Boolean
    val progressDuration: Int
    val title: ResourceString?
    val subtitle: ResourceString?
    val backgroundColor: Color?
    val titleColor: Color?
    val subtitleColor: Color?
    val type: Type
    private val clickListener: Runnable?
    val isVisibleChevron: Boolean

    init {
        type = builder.type
        clickListener = builder.clickListener
        isVisible = builder.isVisible
        title = builder.title
        subtitle = builder.subtitle
        backgroundColor = builder.backgroundColor
        isVisibleChevron = builder.isVisibleChevron
        titleColor = builder.titleColor
        subtitleColor = builder.subtitleColor
        isProgress = builder.isProgress
        progressDuration = builder.progressDuration
    }

    fun getClickListener(): Runnable {
        return clickListener ?: emptyRunnable()
    }

    class Builder(val type: Type) {
        var isVisible = false
            private set
        var title: ResourceString? = null
            private set
        var subtitle: ResourceString? = null
            private set
        var backgroundColor: Color? = null
            private set
        var clickListener: Runnable? = null
            private set
        var isVisibleChevron = false
            private set
        var titleColor: Color? = null
        var subtitleColor: Color? = null
        var isProgress = false
            private set
        var progressDuration = DEFAULT_STATE_BAR_PROGRESS_ANIMATION_DURATION
        fun withVisibility(visibility: Boolean): Builder {
            isVisible = visibility
            return this
        }

        fun withProgress(progress: Boolean): Builder {
            isProgress = progress
            return this
        }

        fun withProgressDuration(progressDuration: Int): Builder {
            this.progressDuration = progressDuration
            return this
        }

        fun withTitle(title: ResourceString?): Builder {
            this.title = title
            return this
        }

        fun withSubtitle(subtitle: ResourceString?): Builder {
            this.subtitle = subtitle
            return this
        }

        fun withBackgroundColor(backgroundColor: Color): Builder {
            this.backgroundColor = backgroundColor
            return this
        }

        fun withTitleColor(titleColor: Color): Builder {
            this.titleColor = titleColor
            return this
        }

        fun withSubtitleColor(subtitleColor: Color): Builder {
            this.subtitleColor = subtitleColor
            return this
        }

        fun withClickListener(clickListener: Runnable?): Builder {
            this.clickListener = clickListener
            return this
        }

        fun withVisibleChevron(visibleChevron: Boolean): Builder {
            isVisibleChevron = visibleChevron
            return this
        }

        fun build(): StateBarViewModel {
            return StateBarViewModel(this)
        }
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val viewModel = o as StateBarViewModel
        return type == viewModel.type
    }

    override fun hashCode(): Int {
        return type.hashCode()
    }

    enum class Type(val priority: Int) {
        BACK_TO_ACTIVE_ORDER(0), ACTIVE_LINKED_ORDER(1), NO_INTERNET_CONNECTION(2), OVERDRAFT(3), LOCATION_ERRORS(
            4
        );

    }

    companion object {
        const val DEFAULT_STATE_BAR_PROGRESS_ANIMATION_DURATION = 500
        fun emptyRunnable(): Runnable {
            return Runnable { empty() }
        }

        fun empty() {
            // no-op
        }
    }
}