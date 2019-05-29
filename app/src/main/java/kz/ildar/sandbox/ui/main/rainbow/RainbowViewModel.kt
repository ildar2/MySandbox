package kz.ildar.sandbox.ui.main.rainbow

import androidx.lifecycle.MutableLiveData
import kz.ildar.sandbox.data.ColorRepository
import kz.ildar.sandbox.data.model.ColorModel
import kz.ildar.sandbox.data.model.RainbowModel
import kz.ildar.sandbox.ui.BaseViewModel
import kotlin.random.Random

class RainbowViewModel(
    private val repository: ColorRepository
) : BaseViewModel() {

    private val random = Random(System.currentTimeMillis())
    val rainbowItemLiveData = MutableLiveData<RainbowModel>()

    fun getNewItem() {
        val item = generateItem()
        rainbowItemLiveData.value = item
    }

    private fun generateItem(): RainbowModel {
        val colorsBuffer = ArrayList(colors)
        colorsBuffer.shuffle(random)
        return RainbowModel(
            random.nextFloat(),
            random.nextFloat(),
            texts[random.nextInt(texts.size)],
            colorsBuffer[0].getColor(),
            colorsBuffer[1].getColor()
        )
    }

    private val texts = repository.getColorList()
        .mapTo(ArrayList(listOf("Хлопок"))) { it.name }
    private val colors = repository.getColorList()
}