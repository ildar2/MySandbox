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

    private val texts = arrayOf(
        "Хлопок",
        "Черный",
        "Серый",
        "Белый",
        "Синий",
        "Зеленый",
        "Красный",
        "Оранжевый",
        "Желтый",
        "Фиолетовый",
        "Голубой",
        "Коричневый"
    )
    private val colors = arrayOf(
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
}