package kz.ildar.sandbox.data.model

import android.graphics.Color
import android.os.Parcelable
import androidx.annotation.ColorInt
import kotlinx.android.parcel.Parcelize

@Parcelize
class ColorModel(
    val alpha: Int,
    val red: Int,
    val green: Int,
    val blue: Int,
    val id: Int = -1,
    val name: String = ""
) : Parcelable {

    fun getHexString() = String.format("#%08X", getColor())

    @ColorInt
    fun getColor() = Color.argb(alpha, red, green, blue)
}