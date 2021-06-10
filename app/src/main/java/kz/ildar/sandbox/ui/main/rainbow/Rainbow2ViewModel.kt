package kz.ildar.sandbox.ui.main.rainbow

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kz.ildar.sandbox.data.ColorRepository
import kz.ildar.sandbox.ui.BaseViewModel
import kz.ildar.sandbox.ui.Status
import kotlin.random.Random

class Rainbow2ViewModel(
    colorRepository: ColorRepository
) : BaseViewModel() {
    private val random = Random(System.currentTimeMillis())
    val rainbowItemLiveData = MutableLiveData<RainbowModel2>()
    private val delay = 1500L
    private val terminate = 60000L
    private var runningJob: Job? = null

    fun start(terminate: Long = this.terminate) {
        uiCaller.set(Status.SHOW_LOADING)
        runningJob = scope.launch {
            launch {
                delay(terminate)
                stop()
                end()
            }
            newItem()
        }
    }

    fun answerYes() {
        val item = rainbowItemLiveData.value ?: return
        uiCaller.set(if (item.answer) Status.SUCCESS else Status.ERROR)
        uiCaller.makeRequest({ delay(1000) }) { newItem() }
    }

    fun answerNo() {
        val item = rainbowItemLiveData.value ?: return
        uiCaller.set(if (!item.answer) Status.SUCCESS else Status.ERROR)
        uiCaller.makeRequest({ delay(1000) }) { newItem() }
    }

    private fun end() {

    }

    fun stop() {
        uiCaller.set(Status.HIDE_LOADING)
        runningJob?.cancel()
    }

    private fun newItem() {
        rainbowItemLiveData.postValue(generateItem())
    }

    private fun generateItem(): RainbowModel2 {
        val colorsBuffer = ArrayList(colors)
        colorsBuffer.shuffle(random)
        val answer = random.nextInt(100) >= 50
        return RainbowModel2(
            colorsBuffer[0].name,
            colorsBuffer[1].getColor(),
            colorsBuffer[2].name,
            if (answer) colorsBuffer[0].getColor() else colorsBuffer[3].getColor(),
            answer,
        )
    }

    private val colors = colorRepository.getColorList()
}

class RainbowModel2(
    val text1: String = "",
    val textColor1: Int = 0,
    val text2: String = "",
    val textColor2: Int = 0,
    val answer: Boolean = true,
)