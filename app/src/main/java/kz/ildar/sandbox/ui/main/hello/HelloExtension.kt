package kz.ildar.sandbox.ui.main.hello

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.runBlocking
import kz.ildar.sandbox.data.HelloRepository
import kz.ildar.sandbox.data.RequestResult
import kz.ildar.sandbox.utils.EventWrapper
import kz.ildar.sandbox.utils.ResourceString
import kz.ildar.sandbox.utils.TextResourceString

interface HelloExtension {
    val greetingLiveData: LiveData<EventWrapper<ResourceString>>
    fun loadGreeting()
    fun loadPersonalGreeting(name: String)
}

@Deprecated("used only when local server is available")
class HelloLocalImpl(
    private val repo: HelloRepository,
    private val errorLiveData: MutableLiveData<EventWrapper<ResourceString>>
) : HelloExtension {

    override val greetingLiveData: MutableLiveData<EventWrapper<ResourceString>> = MutableLiveData()

    override fun loadGreeting() = runBlocking {
        when (val result = repo.greetings()) {
            is RequestResult.Success -> greetingLiveData.postValue(EventWrapper(TextResourceString(result.result?.content)))
            is RequestResult.Error -> errorLiveData.postValue(EventWrapper(result.error))
        }
    }

    override fun loadPersonalGreeting(name: String) = runBlocking {
        when (val result = repo.personalGreeting(name)) {
            is RequestResult.Success -> greetingLiveData.postValue(EventWrapper(TextResourceString(result.result?.content)))
            is RequestResult.Error -> errorLiveData.postValue(EventWrapper(result.error))
        }
    }
}

class HelloEchoImpl(
    private val repo: HelloRepository,
    private val errorLiveData: MutableLiveData<EventWrapper<ResourceString>>
) : HelloExtension {

    override val greetingLiveData: MutableLiveData<EventWrapper<ResourceString>> = MutableLiveData()

    override fun loadGreeting() = runBlocking {
        when (val result = repo.echoGreetings()) {
            is RequestResult.Success -> greetingLiveData.postValue(EventWrapper(TextResourceString(result.result?.args?.content)))
            is RequestResult.Error -> errorLiveData.postValue(EventWrapper(result.error))
        }
    }

    override fun loadPersonalGreeting(name: String) = runBlocking {
        when (val result = repo.echoPersonalGreeting(name)) {
            is RequestResult.Success -> greetingLiveData.postValue(EventWrapper(TextResourceString(result.result?.args?.content)))
            is RequestResult.Error -> errorLiveData.postValue(EventWrapper(result.error))
        }
    }
}