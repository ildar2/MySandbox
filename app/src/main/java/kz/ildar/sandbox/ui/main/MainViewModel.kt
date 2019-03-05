package kz.ildar.sandbox.ui.main

import androidx.lifecycle.MutableLiveData
import kz.ildar.sandbox.data.HelloRepository
import kz.ildar.sandbox.data.RequestResult
import kz.ildar.sandbox.data.model.Parent
import kz.ildar.sandbox.ui.BaseViewModel

class MainViewModel(val repo: HelloRepository) : BaseViewModel() {
    val parentLiveData = MutableLiveData<String>()
    val greetingLiveData = MutableLiveData<String>()

    fun sayHello() = "${repo.giveHello()} from $this"

    override fun toString(): String {
        return "MainViewModel"
    }

    fun getData() {
        val p = Parent()
        parentLiveData.value = p.sayMyName()
    }

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