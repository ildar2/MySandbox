package kz.ildar.sandbox.utils.leetcode.interview

import java.math.BigInteger

/**
 * Let's call any (contiguous) subarray B (of [A]) a mountain if the following properties hold:
 *
 * B.length >= 3
 * There exists some 0 < i < B.length - 1 such that
 * B[0] < B[1] < ... B[i-1] < B[i] > B[i+1] > ... > B[B.length - 1]
 *
 * (Note that B could be any subarray of [A], including the entire array [A].)
 *
 * Given an array [A] of integers, return the length of the longest mountain.
 *
 * Return 0 if there is no mountain.
 *
 * Follow up:
 * - Can you solve it using only one pass?
 * - Can you solve it in O(1) space?
 *
 * a: t
 * l: 5
 * c: 5
 * 2,1,4,7,3,2,5
 */
fun longestMountain(A: IntArray): Int {
    return Mountain.getLongestMountain(A)
}
fun longestMountainInternal(A: IntArray): Int {
    if (A.size < 3) return 0

    var ascending = true
    var longestM = 0
    var currentM = 1

    var i = 1
    while (i < A.size) {
//        println("$i: $ascending $longestM $currentM")
        if (A[i] == A[i - 1]) {
            currentM = 1
            i++
            continue
        }
        if (ascending) {
            if (A[i] > A[i - 1]) {
                //one step
                currentM++
            } else {
                if (currentM > 1) {
                    ascending = false
                    currentM++
                    longestM = kotlin.math.max(currentM, longestM)
                }
            }
        } else {
            if (A[i] < A[i - 1]) {
                //one step
                currentM++
                longestM = kotlin.math.max(currentM, longestM)
            } else {
                ascending = true
                longestM = kotlin.math.max(currentM, longestM)
                currentM = 2
            }
        }
        i++
    }
    return longestM
}

fun fib(n: Int): BigInteger{
    if (n == 0) return BigInteger.ZERO
    if (n == 1) return BigInteger.ONE
    var i = 0
    var previous = BigInteger.ZERO
    var result = BigInteger.ONE
    while (i < n) {
        val temp = result
        result = result.add(previous)
        previous = temp
        i++
    }
    return previous
}
