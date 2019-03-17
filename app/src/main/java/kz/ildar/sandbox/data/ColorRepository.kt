package kz.ildar.sandbox.data

import kz.ildar.sandbox.data.model.ColorModel

class ColorRepository {
    fun getColorList() : List<ColorModel> {
        return ArrayList<ColorModel>().apply {
            add(ColorModel(122, 34, 68, 155))
            add(ColorModel( 122, 34, 255, 55))
            add(ColorModel( 122, 2, 128, 55))
            add(ColorModel( 122, 134, 128, 0))
            add(ColorModel( 255, 34, 128, 55))
            add(ColorModel( 200, 34, 23, 55))
            add(ColorModel( 122, 134, 128, 55))
        }
    }
}