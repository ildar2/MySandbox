package kz.ildar.sandbox.data

import kz.ildar.sandbox.data.model.ColorModel
import timber.log.Timber

class ColorRepository {
    val colorList = ArrayList<ColorModel>().apply {
        add(ColorModel(122, 34, 68, 155, 0, "Bluish"))
        add(ColorModel(176, 220, 50, 23, 1, "Reddish"))
        add(ColorModel(122, 2, 128, 55, 2, "Greenish"))
        add(ColorModel(122, 134, 128, 0, 3, "Sand"))
        add(ColorModel(255, 34, 128, 55, 4, "Green"))
        add(ColorModel(200, 34, 23, 55, 5, "Dark"))
        add(ColorModel(110, 197, 224, 242, 6, "Light"))
    }

    fun getColorList(): List<ColorModel> = colorList

    fun updateColor(color: ColorModel) {
        Timber.w("updateColor called");
        val item = colorList.find { it.id == color.id }
        val index = colorList.indexOf(item)
        colorList[index] = color
    }

    fun createColor(color: ColorModel) {
        colorList.add(color)
    }

    fun removeColor(id: Int) {
        val item = colorList.find { it.id == id }
        colorList.remove(item)
    }
}