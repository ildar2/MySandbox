package kz.ildar.sandbox.ui.main.rainbow

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kz.ildar.sandbox.data.ColorRepository
import kz.ildar.sandbox.data.model.RainbowModel
import kz.ildar.sandbox.ui.BaseViewModel
import kz.ildar.sandbox.ui.Status
import kotlin.random.Random

class RainbowViewModel(
    repository: ColorRepository
) : BaseViewModel() {
    private val random = Random(System.currentTimeMillis())
    val rainbowItemLiveData = MutableLiveData<RainbowModel>()
    val delay = 1500L
    val terminate = 60000L

    fun start(delay: Long = this.delay, terminate: Long = this.terminate) {
        uiCaller.set(Status.SHOW_LOADING)
        scope.launch {
            launch {
                delay(terminate)
                stop()
                success()
            }
            while (true) {
                newItem()
                delay(delay)
            }
        }
    }

    private fun success() {
        uiCaller.set(Status.SUCCESS)
    }

    fun stop() {
        uiCaller.set(Status.HIDE_LOADING)
        coroutineJob.cancelChildren()
    }

    private fun newItem() {
        rainbowItemLiveData.postValue(generateItem())
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