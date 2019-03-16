package kz.ildar.sandbox.ui.main.color

import androidx.lifecycle.MutableLiveData
import kz.ildar.sandbox.ui.BaseViewModel

class ColorViewModel : BaseViewModel() {
    val alphaLiveData = MutableLiveData(255)
    val redLiveData = MutableLiveData(12)
    val greenLiveData = MutableLiveData(36)
    val blueLiveData = MutableLiveData(128)

    fun setAlpha(progress: Int) {
        alphaLiveData.value = progress
    }

    fun setRed(progress: Int) {
        redLiveData.value = progress
    }

    fun setGreen(progress: Int) {
        greenLiveData.value = progress
    }

    fun setBlue(progress: Int) {
        blueLiveData.value = progress
    }
}
