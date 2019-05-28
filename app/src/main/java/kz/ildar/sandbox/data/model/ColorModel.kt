package kz.ildar.sandbox.data.model

import android.graphics.Color
import androidx.annotation.ColorInt

data class ColorModel(
    val alpha: Int,
    val red: Int,
    val green: Int,
    val blue: Int,
    val id: Int = -1,
    val name: String = ""
) {
    @ColorInt
    fun getColor() = Color.argb(alpha, red, green, blue)
}