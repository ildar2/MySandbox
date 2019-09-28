package kz.ildar.sandbox.ui.main.hello

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import kz.ildar.sandbox.data.HelloRepository
import kz.ildar.sandbox.data.RequestResult
import kz.ildar.sandbox.data.model.GreetingsResponse
import kz.ildar.sandbox.di.initTestKoin
import kz.ildar.sandbox.ui.UiCaller
import kz.ildar.sandbox.utils.TextResourceString
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.koin.standalone.KoinComponent
import org.koin.standalone.StandAloneContext.stopKoin
import org.koin.standalone.inject
import org.mockito.Mockito.`when`

class HelloEchoImplTest : KoinComponent {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()//for livedata

    private lateinit var interactor: HelloEchoImpl
    private lateinit var repo: HelloRepository
    private val uiCaller: UiCaller by inject()

    @Before
    fun setUp() {
        initTestKoin()
        repo = mock()
        interactor = HelloEchoImpl(repo)
        interactor.uiCaller = uiCaller
    }

    @After
    fun tearDown() = stopKoin()

    @Test
    fun `test calling loadGreetings with name - should call echoPersonalGreeting`() = runBlocking {
        interactor.loadGreetings("name")

        verify(uiCaller).makeRequest<RequestResult<GreetingsResponse>>(any(), any(), any())
        verify(repo).echoPersonalGreeting("name")
        Unit
    }

    @Test
    fun `test calling loadGreetings without name - should call echoGreetings`() = runBlocking {
        interactor.loadGreetings("")

        verify(uiCaller).makeRequest<RequestResult<GreetingsResponse>>(any(), any(), any())
        verify(repo).echoGreetings()
        Unit
    }

    @Test
    fun `test repo success - should post value to greetingLiveData`() = runBlocking {
        val successValue = mock<GreetingsResponse>()
        `when`(successValue.getContents()).thenReturn("content")
        `when`(repo.echoGreetings()).thenReturn(RequestResult.Success(successValue))
        interactor.loadGreetings("")

        verify(uiCaller).makeRequest<RequestResult<GreetingsResponse>>(any(), any(), any())
        verify(repo).echoGreetings()
        assertEquals(TextResourceString("content"), interactor.greetingLiveData.value?.peek())
        Unit
    }

    @Test
    fun `test repo fail - should post error to uiCaller`() = runBlocking {
        `when`(repo.echoGreetings()).thenReturn(RequestResult.Error(TextResourceString("error")))
        interactor.loadGreetings("")

        verify(uiCaller).makeRequest<RequestResult<GreetingsResponse>>(any(), any(), any())
        verify(repo).echoGreetings()
        verify(uiCaller).setError(TextResourceString("error"))
        Unit
    }
}