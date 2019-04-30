package kz.ildar.sandbox.ui.main.hello

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import kz.ildar.sandbox.data.HelloRepository
import kz.ildar.sandbox.data.RequestResult
import kz.ildar.sandbox.data.model.GreetingsResponse
import kz.ildar.sandbox.ui.UiCaller
import kz.ildar.sandbox.utils.TextResourceString
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito.`when`

class HelloEchoImplTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()//for livedata

    private lateinit var interactor: HelloInteractor
    private lateinit var repo: HelloRepository
    private lateinit var uiCaller: UiCaller

    @Before
    fun setUp() {
        repo = mock()
        uiCaller = mock()
        interactor = HelloEchoImpl(repo, uiCaller)
        doAnswer {
            runBlocking {
                val result = (it.arguments[0] as? suspend CoroutineScope.() -> RequestResult<GreetingsResponse>)?.invoke(this)
                (it.arguments[1] as? suspend (RequestResult<GreetingsResponse>) -> Unit)
                    ?.invoke(result ?: return@runBlocking)
            }
        }.whenever(uiCaller).makeRequest<RequestResult<GreetingsResponse>>(any(), any())
    }

    @Test
    fun `test calling loadGreetings with name - should call echoPersonalGreeting`() = runBlocking {
        interactor.loadGreetings("name")

        verify(uiCaller).makeRequest<RequestResult<GreetingsResponse>>(any(), any())
        verify(repo).echoPersonalGreeting("name")
        Unit
    }

    @Test
    fun `test calling loadGreetings without name - should call echoGreetings`() = runBlocking {
        interactor.loadGreetings("")

        verify(uiCaller).makeRequest<RequestResult<GreetingsResponse>>(any(), any())
        verify(repo).echoGreetings()
        Unit
    }

    @Test
    fun `test repo success - should post value to greetingLiveData`() = runBlocking {
        val successValue = mock<GreetingsResponse>()
        `when`(successValue.getContents()).thenReturn("content")
        `when`(repo.echoGreetings()).thenReturn(RequestResult.Success(successValue))
        interactor.loadGreetings("")

        verify(uiCaller).makeRequest<RequestResult<GreetingsResponse>>(any(), any())
        verify(repo).echoGreetings()
        assertEquals(TextResourceString("content"), interactor.greetingLiveData.value?.peek())
        Unit
    }

    @Test
    fun `test repo fail - should post error to uiCaller`() = runBlocking {
        `when`(repo.echoGreetings()).thenReturn(RequestResult.Error(TextResourceString("error")))
        interactor.loadGreetings("")

        verify(uiCaller).makeRequest<RequestResult<GreetingsResponse>>(any(), any())
        verify(repo).echoGreetings()
        verify(uiCaller).setError(TextResourceString("error"))
        Unit
    }
}