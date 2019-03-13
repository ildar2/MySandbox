package kz.ildar.sandbox.ui.main.hello

import kz.ildar.sandbox.data.HelloRepository
import kz.ildar.sandbox.data.RequestResult
import kz.ildar.sandbox.ui.BaseViewModel
import kz.ildar.sandbox.utils.ResourceString
import kz.ildar.sandbox.utils.SingleLiveEvent
import kz.ildar.sandbox.utils.TextResourceString

class HelloViewModel(val repo: HelloRepository) : BaseViewModel() {
    internal val greetingLiveData = SingleLiveEvent<ResourceString>()

    fun loadGreeting() {
        makeRequest({ repo.greetings() }) {
            when (it) {
                is RequestResult.Success -> greetingLiveData.value = TextResourceString(it.result.content)
                is RequestResult.Error -> errorLiveData.value = TextResourceString(it.error)
            }
        }
    }

    fun loadPersonalGreeting(name: String) {
        makeRequest({ repo.personalGreeting(name) }) {
            when (it) {
                is RequestResult.Success -> greetingLiveData.value = TextResourceString(it.result.content)
                is RequestResult.Error -> errorLiveData.value = TextResourceString(it.error)
            }
        }
    }
}