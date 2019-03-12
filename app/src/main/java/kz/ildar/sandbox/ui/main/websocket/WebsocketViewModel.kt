package kz.ildar.sandbox.ui.main.websocket

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kz.ildar.sandbox.ui.BaseViewModel
import okhttp3.*
import okio.ByteString


class WebsocketViewModel : BaseViewModel() {

    val logLiveData = MutableLiveData<String>()
    val client = OkHttpClient()

    fun start() {
        val request = Request.Builder()
                .url("ws://echo.websocket.org")
                .build()
        val listener = EchoWebSocketListener()
        val ws = client.newWebSocket(request, listener)
        client.dispatcher()
                .executorService()
                .shutdown()
    }
    
    private fun output(text: String) {
        scope.launch(Dispatchers.Main) {
            logLiveData.value = text
        }
    }

    private inner class EchoWebSocketListener : WebSocketListener() {
        private val NORMAL_CLOSURE_STATUS = 1000

        override fun onOpen(webSocket: WebSocket, response: Response) {
            webSocket.send("Hello, it's SSaurel !")
            webSocket.send("What's up ?")
            webSocket.send(ByteString.decodeHex("deadbeef"))
            webSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye !")
        }

        override fun onMessage(webSocket: WebSocket?, text: String?) {
            output("Receiving : " + text!!)
        }

        override fun onMessage(webSocket: WebSocket?, bytes: ByteString) {
            output("Receiving bytes : " + bytes.hex())
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String?) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null)
            output("Closing : $code / $reason")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            output("Error : " + t.message)
        }
    }
}