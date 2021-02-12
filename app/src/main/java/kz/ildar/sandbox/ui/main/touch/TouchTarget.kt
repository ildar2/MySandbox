package kz.ildar.sandbox.ui.main.touch

data class TouchTarget(
    val mode: TouchPanel.Mode,
    val size: Int,
    val xPercent: Float,
    val yPercent: Float,
)