package kz.ildar.sandbox.ui.main.color

import androidx.lifecycle.MutableLiveData
import kz.ildar.sandbox.ui.BaseViewModel

class ColorViewModel : BaseViewModel() {
    val alphaLiveData = MutableLiveData(255)
    val redLiveData = MutableLiveData(12)
    val greenLiveData = MutableLiveData(36)
    val blueLiveData = MutableLiveData(128)
    var currentSeeker = 3

    fun setAlpha(progress: Int) {
        alphaLiveData.value = progress
        currentSeeker = 0
    }

    fun setRed(progress: Int) {
        redLiveData.value = progress
        currentSeeker = 1
    }

    fun setGreen(progress: Int) {
        greenLiveData.value = progress
        currentSeeker = 2
    }

    fun setBlue(progress: Int) {
        blueLiveData.value = progress
        currentSeeker = 3
    }

    fun minusClick() {
        when (currentSeeker) {
            0 -> decrement(alphaLiveData)
            1 -> decrement(redLiveData)
            2 -> decrement(greenLiveData)
            3 -> decrement(blueLiveData)
        }
    }

    fun plusClick() {
        when (currentSeeker) {
            0 -> increment(alphaLiveData)
            1 -> increment(redLiveData)
            2 -> increment(greenLiveData)
            3 -> increment(blueLiveData)
        }
    }

    private fun decrement(liveData: MutableLiveData<Int>) {
        val value = liveData.value ?: 0
        if (value > 0) liveData.value = value - 1
    }

    private fun increment(liveData: MutableLiveData<Int>) {
        val value = liveData.value ?: 0
        if (value < 255) liveData.value = value + 1
    }
}
