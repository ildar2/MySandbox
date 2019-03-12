package kz.ildar.sandbox.ui.main.hello

import androidx.lifecycle.MutableLiveData
import kz.ildar.sandbox.data.HelloRepository
import kz.ildar.sandbox.data.RequestResult
import kz.ildar.sandbox.ui.BaseViewModel

class HelloViewModel(val repo: HelloRepository) : BaseViewModel() {
    val greetingLiveData = MutableLiveData<String>()

    fun loadGreeting() {
        makeRequest({ repo.greetings() }) {
            when (it) {
                is RequestResult.Success -> greetingLiveData.value = it.result.content
                is RequestResult.Error -> errorLiveData.value = it.error
            }
        }
    }

    fun loadPersonalGreeting(name: String) {
        makeRequest({ repo.personalGreeting(name) }) {
            when (it) {
                is RequestResult.Success -> greetingLiveData.value = it.result.content
                is RequestResult.Error -> errorLiveData.value = it.error
            }
        }
    }
}