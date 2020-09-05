package kz.ildar.sandbox.utils.leetcode

import java.lang.UnsupportedOperationException

fun value(): Int{
    try {
        throw UnsupportedOperationException()
        return 0
    } catch (e: Exception) {
        return 1
    } finally {
        println(0)
    }
}