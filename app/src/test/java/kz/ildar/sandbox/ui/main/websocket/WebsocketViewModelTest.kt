package kz.ildar.sandbox.ui.main.websocket

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import kz.ildar.sandbox.di.CoroutineContextProvider
import kz.ildar.sandbox.di.TestContextProvider
import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext

class WebsocketViewModelTest {

    var viewModel: WebsocketViewModel? = null
    private lateinit var client: OkHttpClient
    private lateinit var request: Request

    @Before
    fun setup() {
        client = mock()
        request = mock()

        StandAloneContext.startKoin(//https://proandroiddev.com/testing-with-koin-ade8a46eb4d
                listOf(
                        module {
                            single<CoroutineContextProvider> {
                                TestContextProvider()
                            }
                        }
                )
        )

        viewModel = WebsocketViewModel(client, request)

        //to observe livedata
        //https://proandroiddev.com/how-to-unit-test-livedata-and-lifecycle-components-8a0af41c90d9
        val lifecycle = LifecycleRegistry(mock())
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
    }

    @After
    fun tearDown() {
        StandAloneContext.stopKoin()
    }

    @Test
    fun testCreation() = runBlocking {
        viewModel?.start()

        verify(client).newWebSocket(eq(request), any())
        Unit
    }
}