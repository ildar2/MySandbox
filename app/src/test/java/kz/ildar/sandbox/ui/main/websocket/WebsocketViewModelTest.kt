/**
 * (C) Copyright 2019 Ildar Ishalin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
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
import okhttp3.WebSocket
import okio.ByteString
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext
import org.mockito.Mockito.`when`

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

        viewModel = WebsocketViewModel(client, request, TestContextProvider())

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
        val ws = mock<WebSocket>()
        `when`(client.newWebSocket(eq(request), any())).thenReturn(ws)

        viewModel?.start()

        verify(client).newWebSocket(eq(request), any())

        verify(ws).send("Hello!")
        verify(ws).send("What's up ?")
        verify(ws).send(ByteString.decodeHex("deadbeef"))
        verify(ws).close(1000, "Goodbye!")

        Unit
    }
}