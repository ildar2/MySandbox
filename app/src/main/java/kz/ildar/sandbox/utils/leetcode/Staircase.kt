package kz.ildar.sandbox.utils.leetcode

import kz.ildar.sandbox.utils.FastFibonacci
import java.math.BigInteger

/**
 * There's a staircase with N steps, and you can climb 1 or 2 steps at a time.
 * Given N, write a function that returns the number of unique ways you can climb the staircase.
 * The order of the steps matters.
 *
 * For example, if N is 4, then there are 5 unique ways:
 * 1, 1, 1, 1
 * 2, 1, 1
 * 1, 2, 1
 * 1, 1, 2
 * 2, 2
 *
 * Asked by Amazon
 *
 * @param n = number of steps
 * @return the number of unique ways you can climb the staircase with X = {1, 2}
 */
fun uniqueWaysToClimbStaircase(n: Int): BigInteger {
    return uniqueWaysDirect(n)
}

/**
 * we noticed that f(n) is equal to fibbonacci(n + 1)
 */
fun uniqueWaysDirect(n: Int): BigInteger {
    return FastFibonacci.fastFibonacciDoubling(n + 1)
}

/**
 * recursive solution
 * we split solution in two ways: one for each way we can step
 * time: O(2^N)
 * space: O(2^N)
 */
private fun uniqueWaysNaive(n: Int): Int {
    if (n > 20) throw IllegalArgumentException("recursive approach would take too much time for $n")
    if (n <= 1) return 1
    if (n == 2) return 2

    val one = uniqueWaysNaive(n - 1)
    val two = uniqueWaysNaive(n - 2)
    return one + two
}

/**
 *  What if, instead of being able to climb 1 or 2 steps at a time,
 *  you could climb any number from a set of positive integers X?
 *  For example, if X = {1, 3, 5}, you could climb 1, 3, or 5 steps at a time.
 *  Generalize your function to take in X.
 *
 *  time: O(x.length^n)
 *  space: O(x.length^n)
 */
fun uniqueWaysToClimbStaircaseGeneralized(n: Int, x: Set<Int>): Int {
    if (n < 0) return 0
    if (n <= 1) return 1
    var sum = 0
    for (step in x) {
        sum += uniqueWaysToClimbStaircaseGeneralized(n - step, x)
    }
    return sum
}

// A recursive function used by countWays
fun countWaysUtil(n: Int, m: Int): Int {
    val res = IntArray(n)
    res[0] = 1
    res[1] = 1
    for (i in 2 until n) {
        res[i] = 0
        var j = 1
        while(j <= m && j <= i) {
            res[i] += res[i - j]
            j++
        }
    }
    return res[n - 1]
}

// Returns number of ways to reach s'th stair
fun countWays(s: Int, m: Int): Int {
    return countWaysUtil(s + 1, m)
}
