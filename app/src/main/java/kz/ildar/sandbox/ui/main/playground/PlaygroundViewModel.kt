package kz.ildar.sandbox.ui.main.playground

import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.delay
import kz.ildar.sandbox.ui.BaseViewModel

class PlaygroundViewModel(
    private val vibrator: Vibrator?
) : BaseViewModel() {
    private val vibTime = 5000L

    val vibratingLiveData = MutableLiveData(false)

    fun toggleVibrating() {
        if (vibratingLiveData.value == true) {
            coroutineJob.cancel()
            stopVibrating()
            vibratingLiveData.value = false
        } else {
            uiCaller.makeRequest({ delay(vibTime) }) {
                vibratingLiveData.value = false
            }
            startVibrating()
            vibratingLiveData.value = true
        }
    }

    private fun startVibrating() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(VibrationEffect.createOneShot(vibTime, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            //deprecated in API 26
            vibrator?.vibrate(vibTime)
        }
    }

    private fun stopVibrating() {
        vibrator?.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        stopVibrating()
    }
}