package kz.ildar.sandbox.ui.main.rainbow

import androidx.lifecycle.MutableLiveData
import kz.ildar.sandbox.data.model.ColorModel
import kz.ildar.sandbox.data.model.RainbowModel
import kz.ildar.sandbox.ui.BaseViewModel
import kotlin.random.Random

class RainbowViewModel : BaseViewModel() {
    private val random = Random(System.currentTimeMillis())
    val rainbowItemLiveData = MutableLiveData<RainbowModel>()

    fun getNewItem() {
        val colorsBuffer = arrayListOf(*colors)
        colorsBuffer.shuffle(random)
        val item = RainbowModel(
            random.nextFloat(),
            random.nextFloat(),
            texts[random.nextInt(texts.size)],
            colorsBuffer[0].getColor(),
            colorsBuffer[1].getColor()
        )
        rainbowItemLiveData.value = item
    }

    private val texts = arrayOf("Хлопок", "Синий", "Оранжевый", "Красный", "Зеленый")
    private val colors = arrayOf(
        ColorModel(255, 29, 98, 255, 0, "Blue"),
        ColorModel(176, 212, 100, 0, 1, "Orange"),
        ColorModel(233, 255, 59, 74, 2, "Red"),
        ColorModel(255, 34, 128, 55, 4, "Green")

//        ColorModel(110, 197, 224, 242, 6, "Light"),
//        ColorModel(122, 2, 128, 55, 2, "Greenish"),
//        ColorModel(122, 134, 128, 0, 3, "Sand"),
//        ColorModel(200, 34, 23, 55, 5, "Dark")
    )
}