package kz.ildar.sandbox.utils

import kotlinx.coroutines.*

fun main() {
    checkCoroutines()
}

fun checkCoroutines() {
    val scope: CoroutineScope = CoroutineScope(Dispatchers.Main)
    val job = scope.launch {

        println("start")
        launch {
            for (i in 1..10) {
                println("operation $i")
                delay(100L)
            }
        }

        println("starting another process")
        launch {
            for (i in 1..10) {
                println("another operation $i")
                delay(100L)
            }
        }
        println("end main")
    }
    runBlocking {
        job.join()
    }
}
