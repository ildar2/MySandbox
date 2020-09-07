package kz.ildar.sandbox.utils.leetcode.arrays

import kotlin.math.max

/**
 * Sliding window problem
 *
 * find maximum sum of [k] consecutive elements in [array]
 *
 * input: [1, 4, 2, 10, 23, 3, 1, 0, 20] [k] = 4
 * output: 39 = 4 + 2 + 10 + 23
 */
fun sumOfK(array: IntArray, k: Int): Int {
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