package kz.ildar.sandbox.utils

import java.util.LinkedList
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

interface CommandExecutor {
    fun requestAsync(url: String, onResponse: (String) -> Unit)
}

interface BleDevice {
    fun requestSync(url: String): String
}

class BleDeviceDebug : BleDevice {
    val counter: MutableMap<String, Int> = ConcurrentHashMap()

    override fun requestSync(url: String): String = runBlocking {
        counter.compute(url) { _, v -> (v ?: 0) + 1 }
        delay((200..300L).random())
        "processed $url"
    }
}

class CommandExecutorImpl(
    private val bleDevice: BleDevice,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
) : CommandExecutor {

    private val activeRequests: MutableMap<String, LinkedList<(String) -> Unit>> = ConcurrentHashMap()

    override fun requestAsync(url: String, onResponse: (String) -> Unit) {
        activeRequests.compute(url) { _, activeRequest ->
            if (activeRequest == null) {
                scope.launch {
                    val res = bleDevice.requestSync(url)
                    activeRequests.remove(url)?.forEach {
                        it.invoke(res)
                    }
                }
                LinkedList<(String) -> Unit>().apply { add(onResponse) }
            } else {
                activeRequest.apply { add(onResponse) }
            }
        }
    }
}

fun main() {
    val callbackCounter = AtomicInteger(0)
    val bleDevice = BleDeviceDebug()
    val executor: CommandExecutor = CommandExecutorImpl(bleDevice)

    val attempts = 100
    repeat(attempts) { k ->
        println("requesting $k")
        val num = (0..10).random()
        executor.requestAsync("device_$num") {
            callbackCounter.incrementAndGet()
            println("first result$k: $it")
        }
    }
    Thread.sleep(1000)
    repeat(attempts) { k ->
        println("requesting $k again")
        val num = (0..10).random()
        executor.requestAsync("device_$num") {
            callbackCounter.incrementAndGet()
            println("result$k: $it")
        }
    }
    Thread.sleep(1000)
    println("callbackCounter: $callbackCounter")
    println("request counter: ${bleDevice.counter}")
}
