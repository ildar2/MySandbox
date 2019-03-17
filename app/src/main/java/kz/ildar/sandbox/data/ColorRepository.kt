package kz.ildar.sandbox.data

import kz.ildar.sandbox.data.model.ColorModel

class ColorRepository {
    fun getColorList() : List<ColorModel> {
        return ArrayList<ColorModel>().apply {
            add(ColorModel(1, 122, 34, 128, 55))
            add(ColorModel(2, 122, 34, 255, 55))
            add(ColorModel(3, 122, 2, 128, 55))
            add(ColorModel(4, 122, 34, 128, 0))
            add(ColorModel(5, 255, 34, 128, 55))
            add(ColorModel(6, 200, 34, 23, 55))
            add(ColorModel(7, 122, 134, 128, 55))
        }
    }
}