package kz.ildar.sandbox.data

import kz.ildar.sandbox.data.model.ColorModel

class ColorRepository {
    //todo room db
    private val colorListOld = ArrayList(
        listOf(
            ColorModel(122, 34, 68, 155, 0, "Bluish"),
            ColorModel(176, 220, 50, 23, 1, "Reddish"),
            ColorModel(122, 2, 128, 55, 2, "Greenish"),
            ColorModel(122, 134, 128, 0, 3, "Sand"),
            ColorModel(255, 34, 128, 55, 4, "Green"),
            ColorModel(200, 34, 23, 55, 5, "Dark"),
            ColorModel(110, 197, 224, 242, 6, "Light")
        )
    )
    private val colorList = ArrayList(
        listOf(
            ColorModel(255, 0, 0, 0, name = "Черный"),
            ColorModel(255, 188, 188, 188, name = "Серый"),
            ColorModel(255, 255, 255, 255, name = "Белый"),
            ColorModel(255, 29, 98, 255, name = "Синий"),
            ColorModel(255, 34, 128, 55, name = "Зеленый"),
            ColorModel(233, 192, 0, 0, name = "Красный"),
            ColorModel(176, 255, 102, 0, name = "Оранжевый"),
            ColorModel(192, 228, 228, 0, name = "Желтый"),
            ColorModel(255, 90, 0, 157, name = "Фиолетовый"),
            ColorModel(255, 117, 187, 253, name = "Голубой"),
            ColorModel(255, 101, 67, 33, name = "Коричневый")
        )
    )

    fun getColorList(): List<ColorModel> = colorList

    fun updateColor(color: ColorModel) {
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