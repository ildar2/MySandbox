package kz.ildar.sandbox.ui.main.hello

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kz.ildar.sandbox.data.HelloRepository
import kz.ildar.sandbox.data.RequestResult
import kz.ildar.sandbox.utils.EventWrapper
import kz.ildar.sandbox.utils.ResourceString
import kz.ildar.sandbox.utils.TextResourceString

interface HelloInteractor {
    val greetingLiveData: LiveData<EventWrapper<ResourceString>>
    suspend fun loadGreeting(errorLiveData: MutableLiveData<EventWrapper<ResourceString>>)
    suspend fun loadPersonalGreeting(name: String, errorLiveData: MutableLiveData<EventWrapper<ResourceString>>)
}

class HelloLocalImpl(
    private val repo: HelloRepository
) : HelloInteractor {

    override val greetingLiveData: MutableLiveData<EventWrapper<ResourceString>> = MutableLiveData<EventWrapper<ResourceString>>()

    override suspend fun loadGreeting(errorLiveData: MutableLiveData<EventWrapper<ResourceString>>) {
        val result = repo.greetings()
        when (result) {
            is RequestResult.Success -> greetingLiveData.postValue(EventWrapper(TextResourceString(result.result?.content)))
            is RequestResult.Error -> errorLiveData.postValue(EventWrapper(result.error))
        }
    }

    override suspend fun loadPersonalGreeting(name: String, errorLiveData: MutableLiveData<EventWrapper<ResourceString>>) {
        val result = repo.personalGreeting(name)
        when (result) {
            is RequestResult.Success -> greetingLiveData.postValue(EventWrapper(TextResourceString(result.result?.content)))
            is RequestResult.Error -> errorLiveData.postValue(EventWrapper(result.error))
        }
    }
}

class HelloEchoImpl(
    private val repo: HelloRepository
) : HelloInteractor {

    override val greetingLiveData: MutableLiveData<EventWrapper<ResourceString>> = MutableLiveData<EventWrapper<ResourceString>>()

    override suspend fun loadGreeting(errorLiveData: MutableLiveData<EventWrapper<ResourceString>>) {
        val result = repo.echoGreetings()
        when (result) {
            is RequestResult.Success -> greetingLiveData.postValue(EventWrapper(TextResourceString(result.result?.args?.content)))
            is RequestResult.Error -> errorLiveData.postValue(EventWrapper(result.error))
        }
    }

    override suspend fun loadPersonalGreeting(name: String, errorLiveData: MutableLiveData<EventWrapper<ResourceString>>) {
        val result = repo.echoPersonalGreeting(name)
        when (result) {
            is RequestResult.Success -> greetingLiveData.postValue(EventWrapper(TextResourceString(result.result?.args?.content)))
            is RequestResult.Error -> errorLiveData.postValue(EventWrapper(result.error))
        }
    }
}