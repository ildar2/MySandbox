package kz.ildar.sandbox.ui.main.websocket

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.runBlocking
import kz.ildar.sandbox.di.CoroutineContextProvider
import kz.ildar.sandbox.di.TestContextProvider
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext

class WebsocketViewModelTest {

    var viewModel: WebsocketViewModel? = null
//    private lateinit var formManager: FormManager

    @Before
    fun setup() {
//        formManager = mock()

        StandAloneContext.startKoin(//https://proandroiddev.com/testing-with-koin-ade8a46eb4d
                listOf(
                        module {
                            single<CoroutineContextProvider> {
                                TestContextProvider()
                            }
//                            factory { formManager }
                        }
                )
        )

        viewModel = WebsocketViewModel(

        )

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

    }
}