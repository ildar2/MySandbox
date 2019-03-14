package kz.ildar.sandbox.ui.main.hello

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

class HelloViewModel(private val repo: HelloRepository, private val multiRepo: MultiCallRepository) : BaseViewModel() {

    private val _greetingLiveData = MutableLiveData<Event<ResourceString>>()
    internal val greetingLiveData: LiveData<Event<ResourceString>>
        get() = _greetingLiveData

    fun loadGreetings(name: String) {
        Timber.w("loadGreetings called")
        if (name.isBlank()) {
            loadEchoGreeting()
        } else {
            loadEchoPersonalGreeting(name)
        }
    }

    private fun loadEchoGreeting() {
        makeRequest({ repo.echoGreetings() }) {
            when (it) {
                is RequestResult.Success -> _greetingLiveData.value = Event(TextResourceString(it.result?.args?.content))
                is RequestResult.Error -> this setError it.error
            }
        }
    }

    private fun loadEchoPersonalGreeting(name: String) {
        makeRequest({ repo.echoPersonalGreeting(name) }) {
            when (it) {
                is RequestResult.Success -> _greetingLiveData.value = Event(TextResourceString(it.result?.args?.content))
                is RequestResult.Error -> this setError it.error
            }
        }
    }

    private fun loadGreeting() {
        makeRequest({ repo.greetings() }) {
            when (it) {
                is RequestResult.Success -> _greetingLiveData.value = Event(TextResourceString(it.result?.content))
                is RequestResult.Error -> this setError it.error
            }
        }
    }

    private fun loadPersonalGreeting(name: String) {
        makeRequest({ repo.personalGreeting(name) }) {
            when (it) {
                is RequestResult.Success -> _greetingLiveData.value = Event(TextResourceString(it.result?.content))
                is RequestResult.Error -> this setError it.error
            }
        }
    }
}