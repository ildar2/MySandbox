package kz.ildar.sandbox.ui.main.hello

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kz.ildar.sandbox.data.HelloRepository
import kz.ildar.sandbox.data.RequestResult
import kz.ildar.sandbox.ui.BaseViewModel
import kz.ildar.sandbox.utils.Event
import kz.ildar.sandbox.utils.ResourceString
import kz.ildar.sandbox.utils.TextResourceString

class HelloViewModel(val repo: HelloRepository) : BaseViewModel() {

    private val _greetingLiveData = MutableLiveData<Event<ResourceString>>()
    internal val greetingLiveData: LiveData<Event<ResourceString>>
        get() = _greetingLiveData

    fun loadGreeting() {
        makeRequest({ repo.greetings() }) {
            when (it) {
                is RequestResult.Success -> _greetingLiveData.value = Event(TextResourceString(it.result?.content))
                is RequestResult.Error -> this setError TextResourceString(it.error)
            }
        }
    }

    fun loadPersonalGreeting(name: String) {
        makeRequest({ repo.personalGreeting(name) }) {
            when (it) {
                is RequestResult.Success -> _greetingLiveData.value = Event(TextResourceString(it.result?.content))
                is RequestResult.Error -> this setError TextResourceString(it.error)
            }
        }
    }
}