package kz.ildar.sandbox.utils.leetcode.interview

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect

fun interface Consumer {
    fun consume(data: String)
}

internal class JavaLibrary {
    fun subscribe(consumer: Consumer) = runBlocking{
        //sоme lоgiс
        consumer.consume("start")
        delay(100)
        consumer.consume("first")
        delay(100)
        consumer.consume("second")
        delay(100)
        consumer.consume("third")
        delay(100)
        consumer.consume("fourth")
        delay(100)
        consumer.consume("fifth")
    }

    fun unSubscribe(consumer: Consumer) {
        //sоme lоgiс
        println("unsubscribing")
    }
}

class UseCase {

    private val library = JavaLibrary()

    suspend operator fun invoke(): Flow<String> = channelFlow {
        val consumer = Consumer { data -> channel.offer(data) }
        launch {
            library.subscribe(consumer)
        }
        awaitClose() {
            library.unSubscribe(consumer)
        }
    }
}

fun main() = runBlocking {
    println("start main")
    val useCase = UseCase()
    val job = launch {
        useCase().collect {
            println("message: $it")
        }
    }
    delay(400)
    job.cancelAndJoin()
    println("end main")
}
