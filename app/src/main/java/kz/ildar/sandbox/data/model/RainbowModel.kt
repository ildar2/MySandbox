package kz.ildar.sandbox.data.model

data class RainbowModel(
    val x: Float = 0F,
    val y: Float = 0F,
    val text: String = "",
    val textColor: Int = 0,
    val bgColor: Int = 0
) {
    override fun toString(): String = "$text $x $y"
}