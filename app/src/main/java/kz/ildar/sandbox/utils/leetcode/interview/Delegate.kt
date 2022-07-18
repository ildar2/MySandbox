package kz.ildar.sandbox.utils.leetcode.interview

interface Logger {
    fun tag(): String
    fun log(message: String)
}

class DefaultLogger : Logger {
    override fun tag() = "Default Tag"
    override fun log(message: String) {
        println("${tag()} $message")
    }
}

class MyViewModel : Logger by DefaultLogger() {
    override fun tag(): String = "My tag"
}

fun main() {
    MyViewModel().log("text")
}
