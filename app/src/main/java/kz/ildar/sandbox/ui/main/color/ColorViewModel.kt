package kz.ildar.sandbox.ui.main.color

import android.graphics.Color
import android.os.Parcelable
import androidx.annotation.ColorInt
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.android.parcel.Parcelize
import kz.ildar.sandbox.data.ColorRepository
import kz.ildar.sandbox.data.model.ColorModel
import kz.ildar.sandbox.ui.BaseViewModel

class ColorViewModel(
    private val colorRepository: ColorRepository
) : BaseViewModel() {

    private val _colorLiveData = MutableLiveData(ColorMutable(255, 12, 36, 128))
    val colorLiveData = MediatorLiveData<ColorMutable>()

    init {
        colorLiveData.addSource(_colorLiveData) {
            if (it.id != -1) {
                colorRepository.updateColor(it.toColorModel())
            }
            colorLiveData.value = it
        }
    }

    private var currentSeeker = 3

    fun setAlpha(progress: Int) {
        _colorLiveData.value = _colorLiveData.value!!.apply { alpha = progress }
        currentSeeker = 0
    }

    fun setRed(progress: Int) {
        _colorLiveData.value = _colorLiveData.value!!.apply { red = progress }
        currentSeeker = 1
    }

    fun setGreen(progress: Int) {
        _colorLiveData.value = _colorLiveData.value!!.apply { green = progress }
        currentSeeker = 2
    }

    fun setBlue(progress: Int) {
        _colorLiveData.value = _colorLiveData.value!!.apply { blue = progress }
        currentSeeker = 3
    }

    fun minusClick() {
        val color = _colorLiveData.value!!
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
        _colorLiveData.value = color
    }

    fun plusClick() {
        val color = _colorLiveData.value!!
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
        _colorLiveData.value = color
    }

    fun initColor(colorMutable: ColorMutable) {
        _colorLiveData.value = colorMutable
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

    fun toColorModel(): ColorModel {
        return ColorModel(
            alpha,
            red,
            green,
            blue,
            id,
            name
        )
    }

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
