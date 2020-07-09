package kz.ildar.sandbox.ui.main.rainbow

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kz.ildar.sandbox.data.ColorRepository
import kz.ildar.sandbox.data.model.RainbowModel
import kz.ildar.sandbox.ui.BaseViewModel
import kz.ildar.sandbox.ui.Status
import kotlin.random.Random

class RainbowViewModel(
    colorRepository: ColorRepository
) : BaseViewModel() {
    private val random = Random(System.currentTimeMillis())
    val rainbowItemLiveData = MutableLiveData<RainbowModel>()
    private val delay = 1500L
    private val terminate = 60000L
    private var runningJob: Job? = null

    fun start(delay: Long = this.delay, terminate: Long = this.terminate) {
        uiCaller.set(Status.SHOW_LOADING)
        runningJob = scope.launch {
            launch {
                delay(terminate)
                stop()
                success()
            }
            while(true) {
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
        runningJob?.cancel()
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

    private val texts = colorRepository.getColorList()
        .mapTo(ArrayList(listOf("Хлопок"))) { it.name }
    private val colors = colorRepository.getColorList()
}