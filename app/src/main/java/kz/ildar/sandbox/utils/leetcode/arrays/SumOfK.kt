package kz.ildar.sandbox.utils.leetcode.arrays

import kotlin.math.max

/**
 * Sliding window problem
 *
 * find maximum sum of [k] consecutive elements in [array]
 *
 * input: [1, 4, 2, 10, 23, 3, 1, 0, 20] [k] = 4
 * output: 39
 * explanation: 4 + 2 + 10 + 23 = 39
 */
fun sumOfK(array: IntArray, k: Int): Int {
    return sumOfKOptimal(array, k)
}

fun sumOfKNaive(array: IntArray, k: Int): Int {
    if (k > array.size) return -1//invalid

    var maxSum = Int.MIN_VALUE

    for (i in 0 until array.size - k + 1) {
        var currentSum = 0
        for (j in i until i + k) {
            currentSum += array[j]
        }
        maxSum = max(currentSum, maxSum)
    }

    return maxSum
}

fun sumOfKOptimal(array: IntArray, k: Int): Int {
    if (k > array.size) return -1//invalid
    var maxSum = 0
    for (i in 0 until k) {
        maxSum += array[i]
    }

    var current = maxSum
    for (i in k until array.size) {
        current += array[i] - array[i - k]
        maxSum = max(current, maxSum)
    }

    return maxSum
}