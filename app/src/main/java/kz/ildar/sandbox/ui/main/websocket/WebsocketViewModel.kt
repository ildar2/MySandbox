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

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import kz.ildar.sandbox.ui.BaseViewModel
import okhttp3.*
import okio.ByteString
import okio.ByteString.Companion.decodeHex

class WebsocketViewModel(
    private val client: OkHttpClient,
    private val request: Request
) : BaseViewModel() {
    private val NORMAL_CLOSURE_STATUS = 1000

    val logLiveData = MutableLiveData<String>()
    private lateinit var webSocket: WebSocket

    fun start() {
        val listener = EchoWebSocketListener()
        webSocket = client.newWebSocket(request, listener)
        webSocket.send("Hello!")
        webSocket.send("What's up ?")
        webSocket.send("deadbeef".decodeHex())
        webSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye!")
    }

    private fun output(text: String) = scope.launch(coroutineProvider.Main) {
        logLiveData.value = text
    }

    private inner class EchoWebSocketListener : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: Response) {
            output("onOpen")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            output("Receiving : $text")
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            output("Receiving bytes : ${bytes.hex()}")
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null)
            output("Closing : $code / $reason")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            output("Error : ${t.message}")
        }
    }
}