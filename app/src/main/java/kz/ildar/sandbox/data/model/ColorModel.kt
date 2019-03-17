package kz.ildar.sandbox.data.model

import android.graphics.Color
import androidx.annotation.ColorInt

class ColorModel(
    val id: Int,
    val alpha: Int,
    val red: Int,
    val green: Int,
    val blue: Int,
    val name: String = ""
) {

    fun getHexString() = String.format("#%08X", getColor())

    @ColorInt
    fun getColor() = Color.argb(alpha, red, green, blue)
}