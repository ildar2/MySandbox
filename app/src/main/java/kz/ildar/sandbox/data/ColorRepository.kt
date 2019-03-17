package kz.ildar.sandbox.data

import kz.ildar.sandbox.data.model.ColorModel

class ColorRepository {
    fun getColorList() : List<ColorModel> {
        return ArrayList<ColorModel>().apply {
            add(ColorModel(122, 34, 68, 155, name = "Bluish"))
            add(ColorModel( 176, 220, 50, 23, name = "Reddish"))
            add(ColorModel( 122, 2, 128, 55, name = "Greenish"))
            add(ColorModel( 122, 134, 128, 0, name = "Sand"))
            add(ColorModel( 255, 34, 128, 55, name = "Green"))
            add(ColorModel( 200, 34, 23, 55, name = "Dark"))
            add(ColorModel( 110, 197, 224, 242, name = "Light"))
        }
    }
}