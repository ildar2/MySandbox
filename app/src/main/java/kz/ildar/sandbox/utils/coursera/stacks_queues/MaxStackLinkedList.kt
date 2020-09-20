package kz.ildar.sandbox.utils.coursera.stacks_queues

import edu.princeton.cs.algs4.StdIn
import kotlin.math.max

class MaxStackLinkedList {

    private var first: Node? = null
    private var max = Int.MIN_VALUE

    class Node(
        val value: Int,
        var ref: Node? = null
    )

    fun push(item: Int) {
        if (first == null) {
            first = Node(item)
            max = item
            return
        }
        max = max(max, item)
        first = Node(item, first)
    }

    fun pop(): Int {
        if (first == null) throw NoSuchElementException("popped from an empty stack")
        val ans = first!!.value
        if (ans == max) {
            max = Int.MIN_VALUE
            var next = first?.ref
            while(next != null) {
                max = max(max, next.value)
                next = next.ref
            }
        }
        first = first?.ref
        return ans
    }

    fun max(): Int = max

    override fun toString(): String {
        if (first == null) return "Stack is empty"
        val sb = StringBuilder()
        sb.append(first!!.value)
        var next = first!!.ref
        while(next != null) {
            sb.append(" ").append(next.value)
            next = next.ref
        }
        return sb.toString()
    }
}

fun main() {
    val stack = MaxStackLinkedList()
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