package kz.ildar.sandbox.ui.main.hello

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kz.ildar.sandbox.data.HelloRepository
import kz.ildar.sandbox.data.RequestResult
import kz.ildar.sandbox.utils.Event
import kz.ildar.sandbox.utils.ResourceString
import kz.ildar.sandbox.utils.TextResourceString

interface HelloInteractor {
    val greetingLiveData: LiveData<Event<ResourceString>>
    suspend fun loadGreeting(errorLiveData: MutableLiveData<Event<ResourceString>>)
    suspend fun loadPersonalGreeting(name: String, errorLiveData: MutableLiveData<Event<ResourceString>>)
}

class HelloLocalImpl(
    private val repo: HelloRepository
) : HelloInteractor {

    override val greetingLiveData: MutableLiveData<Event<ResourceString>> = MutableLiveData<Event<ResourceString>>()

    override suspend fun loadGreeting(errorLiveData: MutableLiveData<Event<ResourceString>>) {
        val result = repo.greetings()
        when (result) {
            is RequestResult.Success -> greetingLiveData.postValue(Event(TextResourceString(result.result?.content)))
            is RequestResult.Error -> errorLiveData.postValue(Event(result.error))
        }
    }

    override suspend fun loadPersonalGreeting(name: String, errorLiveData: MutableLiveData<Event<ResourceString>>) {
        val result = repo.personalGreeting(name)
        when (result) {
            is RequestResult.Success -> greetingLiveData.postValue(Event(TextResourceString(result.result?.content)))
            is RequestResult.Error -> errorLiveData.postValue(Event(result.error))
        }
    }
}

class HelloEchoImpl(
    private val repo: HelloRepository
) : HelloInteractor {

    override val greetingLiveData: MutableLiveData<Event<ResourceString>> = MutableLiveData<Event<ResourceString>>()

    override suspend fun loadGreeting(errorLiveData: MutableLiveData<Event<ResourceString>>) {
        val result = repo.echoGreetings()
        when (result) {
            is RequestResult.Success -> greetingLiveData.postValue(Event(TextResourceString(result.result?.args?.content)))
            is RequestResult.Error -> errorLiveData.postValue(Event(result.error))
        }
    }

    override suspend fun loadPersonalGreeting(name: String, errorLiveData: MutableLiveData<Event<ResourceString>>) {
        val result = repo.echoPersonalGreeting(name)
        when (result) {
            is RequestResult.Success -> greetingLiveData.postValue(Event(TextResourceString(result.result?.args?.content)))
            is RequestResult.Error -> errorLiveData.postValue(Event(result.error))
        }
    }
}