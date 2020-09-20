package kz.ildar.sandbox.utils.coursera.stacks_queues

import edu.princeton.cs.algs4.StdIn
import kotlin.math.max

class MaxStackArray {

    private var n = 0
    private var max = Int.MIN_VALUE
    private var items = IntArray(1)

    fun push(item: Int) {
        if (n == items.size) resize(n * 2)
        items[n++] = item

        max = max(max, item)
    }

    private fun resize(size: Int) {
        items = IntArray(size) { i ->
            if (i < n) items[i] else 0
        }
        println("resize to $size happened: $this")
    }

    fun pop(): Int {
        if (n <= 0) throw ArrayIndexOutOfBoundsException("Stack is empty")
        if (n == items.size / 4) resize(items.size / 2)
        if (max == items[n - 1]) {
            max = Int.MIN_VALUE
            for (i in 0 until n - 1) {
                max = max(max, items[i])
            }
        }
        return items[--n]
    }

    fun max(): Int = max

    override fun toString(): String = "$n ${items.filterIndexed { i, _ -> i < n }}}"
}

fun main() {
    val stack = MaxStackArray()
    println(stack)
    stack.push(6)
    stack.push(4)
    println(stack)
    stack.push(5)
    println(stack)
    println("pop: " + stack.pop())
    println(stack)

    stack.push(8)
    stack.push(9)
    stack.push(10)
    println(stack)

    while(true) {
        when (val input = StdIn.readString()) {
            "-" -> println("pop: " + stack.pop())
            "m" -> println("max: " + stack.max())
            else -> {
                stack.push(input.toInt())
                println("pushed $input")
            }
        }
        println(stack)
    }
}