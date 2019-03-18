package kz.ildar.sandbox.ui.main.color

import android.graphics.Color
import android.os.Parcelable
import androidx.annotation.ColorInt
import androidx.lifecycle.MutableLiveData
import kotlinx.android.parcel.Parcelize
import kz.ildar.sandbox.data.model.ColorModel
import kz.ildar.sandbox.di.CoroutineContextProvider
import kz.ildar.sandbox.ui.BaseViewModel

class ColorViewModel(contextProvider: CoroutineContextProvider) : BaseViewModel(contextProvider) {
    val colorLiveData = MutableLiveData(ColorMutable(255, 12, 36, 128))
    private var currentSeeker = 3

    fun setAlpha(progress: Int) {
        val color = colorLiveData.value
        color!!.alpha = progress
        colorLiveData.value = color
        currentSeeker = 0
    }

    fun setRed(progress: Int) {
        val color = colorLiveData.value
        color!!.red = progress
        colorLiveData.value = color
        currentSeeker = 1
    }

    fun setGreen(progress: Int) {
        val color = colorLiveData.value
        color!!.green = progress
        colorLiveData.value = color
        currentSeeker = 2
    }

    fun setBlue(progress: Int) {
        val color = colorLiveData.value
        color!!.blue = progress
        colorLiveData.value = color
        currentSeeker = 3
    }

    fun minusClick() {
        val color = colorLiveData.value!!
        when (currentSeeker) {
            0 -> {
                val value = color.alpha
                if (value > 0) color.alpha = value - 1
            }
            1 -> {
                val value = color.red
                if (value > 0) color.red = value - 1
            }
            2 -> {
                val value = color.green
                if (value > 0) color.green = value - 1
            }
            3 -> {
                val value = color.blue
                if (value > 0) color.blue = value - 1
            }
        }
        colorLiveData.value = color
    }

    fun plusClick() {
        val color = colorLiveData.value!!
        when (currentSeeker) {
            0 -> {
                val value = color.alpha
                if (value < 255) color.alpha = value + 1
            }
            1 -> {
                val value = color.red
                if (value < 255) color.red = value + 1
            }
            2 -> {
                val value = color.green
                if (value < 255) color.green = value + 1
            }
            3 -> {
                val value = color.blue
                if (value < 255) color.blue = value + 1
            }
        }
        colorLiveData.value = color
    }
}

@Parcelize
class ColorMutable(
    var alpha: Int,
    var red: Int,
    var green: Int,
    var blue: Int,
    val id: Int = -1,
    val name: String = ""
) : Parcelable {

    fun getHexString() = String.format("#%08X", getColor())

    @ColorInt
    fun getColor() = Color.argb(alpha, red, green, blue)

    companion object {
        fun from(item: ColorModel): ColorMutable {
            return ColorMutable(
                item.alpha,
                item.red,
                item.green,
                item.blue,
                item.id,
                item.name
            )
        }
    }
}
