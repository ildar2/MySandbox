package kz.ildar.sandbox.utils.leetcode.arrays

import kotlin.concurrent.thread

const val NO_JUDGE = -1

/**
 * there is a town, population [n]
 * all people labeled 1 - n
 * there might be a judge, rules for judge:
 * 1. everybody trusts judge
 * 2. judge trusts nobody
 *
 * return number of the judge or -1
 */
fun findJudge(n: Int, trust: Array<IntArray>): Int {
    return findJudgeNaive(n, trust)
}

const val DEFINITELY_NOT_A_JUDGE = -1

/**
 * store trust in hashmap, then loop through it to find valid candidates
 * time: O(n)
 * space: O(n)
 * no input modification
 */
fun findJudgeNaive(N: Int, trust: Array<IntArray>): Int {

    val candidates = IntArray(N)//-1 if not a judge, +1 for every citizen
    for (pair in trust) {
        candidates[pair[0] - 1] = DEFINITELY_NOT_A_JUDGE
        if (candidates[pair[1] - 1] >= 0) {
            candidates[pair[1] - 1]++
        }
    }
    for (i in candidates.indices) {
        if (candidates[i] == N - 1) return i + 1
    }
    return NO_JUDGE
}

fun main() {
}

@Volatile
var counter = 0

fun checkMultithreading() {
    for (i in 1..150) {
        thread {
            counter++
        }
    }
    Thread.sleep(20)
    println(counter)
}

class Point(val x: Int, val y: Int)

fun checking1() {
    val point1 = Point(1, 2)
    val point2 = Point(1, 2)
    println(point1 == point2)
    println(point1 === point2)
}

fun checking2() {
    listOf("three", "two", "one").forEach {
        if (it == "one") {
            return
        } else {
            println(it)
        }
    }
    println("go!")
}
