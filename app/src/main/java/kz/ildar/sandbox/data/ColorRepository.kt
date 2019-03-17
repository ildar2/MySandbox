package kz.ildar.sandbox.data

import kz.ildar.sandbox.data.model.ColorModel

class ColorRepository {
    fun getColorList() : List<ColorModel> {
        return ArrayList<ColorModel>().apply {
            add(ColorModel(1, 122, 34, 128, 55))
            add(ColorModel(1, 122, 34, 128, 55))
            add(ColorModel(1, 122, 34, 128, 55))
            add(ColorModel(1, 122, 34, 128, 55))
            add(ColorModel(1, 122, 34, 128, 55))
            add(ColorModel(1, 122, 34, 128, 55))
            add(ColorModel(1, 122, 34, 128, 55))
        }
    }
}