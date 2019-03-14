package kz.ildar.sandbox.ui.main.multiCall

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kz.ildar.sandbox.data.HelloRepository
import kz.ildar.sandbox.data.MultiCallRepository
import kz.ildar.sandbox.data.RequestResult
import kz.ildar.sandbox.ui.BaseViewModel
import kz.ildar.sandbox.utils.Event
import kz.ildar.sandbox.utils.ResourceString
import kz.ildar.sandbox.utils.TextResourceString
import timber.log.Timber

class MultiCallViewModel(private val repo: HelloRepository, private val multiRepo: MultiCallRepository) : BaseViewModel() {

    private val _logLiveData = MutableLiveData<Event<ResourceString>>()
    internal val logLiveData: LiveData<Event<ResourceString>>
        get() = _logLiveData

    fun multiCall() {
        makeRequest({ multiRepo.callAllMethods() }) { result ->
            result.forEach {
                _logLiveData.value = when (it) {
                    is RequestResult.Success -> Event(TextResourceString(it.result?.getContents()))
                    is RequestResult.Error -> Event(it.error)
                }
            }
        }
    }

    fun twoCall() {
        makeRequest({ multiRepo.callTwoMethods() }) { result ->
            result.forEach {
                _logLiveData.value = when (it) {
                    is RequestResult.Success -> Event(TextResourceString(it.result?.getContents()))
                    is RequestResult.Error -> Event(it.error)
                }
            }
        }
    }

    fun threeCall() {
        makeRequest({ multiRepo.callThreeMethods() }) { result ->
            result.forEach {
                _logLiveData.value = when (it) {
                    is RequestResult.Success -> Event(TextResourceString(it.result?.getContents()))
                    is RequestResult.Error -> Event(it.error)
                }
            }
        }
    }

    fun arrayCall() {
        makeRequest({ multiRepo.callArrayOfMethods() }) { result ->
            result.forEach {
                _logLiveData.value = when (it) {
                    is RequestResult.Success -> Event(TextResourceString(it.result?.getContents()))
                    is RequestResult.Error -> Event(it.error)
                }
            }
        }
    }
}