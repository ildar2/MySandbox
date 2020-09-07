package kz.ildar.sandbox.utils.leetcode.arrays

import kotlin.math.abs

/**
 * Given two arrays [a], [b] of length N and a [target]
 * find a pair of numbers from each array
 * sum of which is closest to [target]
 *
 * [-1, 3, 8, 2, 9, 5]
 * [4, 1, 2, 10, 5, 20]
 * target: 24
 */
fun closestToSum(a: IntArray, b: IntArray, target: Int): Pair<Int, Int> {
    if (a.isEmpty() || b.isEmpty()) {
        return Pair(-1, -1)
    }
    return closestToSumOptimal(a, b, target)
}

/**
 * build N*N matrix of sorted arrays
 * traverse the matrix diagonally keeping track of the closest to [target]
 *
 * time: O(N*log(N))
 * space: O(1)
 * modifies arrays - can copy arrays, so space would be O(N)
 *
 * [-1, 2, 3, 5, 8, 9]
 * [1, 2, 4, 5, 10, 20]
 */
private fun closestToSumOptimal(a: IntArray, b: IntArray, target: Int): Pair<Int, Int> {
    a.copyOf()
    a.sort()
    b.sort()

    var closest = Pair(a[0], b[0])
    var i = 0
    var j = b.size - 1

    while(i < a.size && j >= 0) {
        if (abs(a[i] + b[j] - target) < abs(closest.first + closest.second - target)) {
            closest = Pair(a[i], b[j])
        }
        if (a[i] + b[j] < target) i++
        else j--
    }
    return closest
}

/**
 * brute force approach
 * remember closest sum and traverse all possible pairs
 *
 * time: O(N^2)
 * space: O(1)
 */
private fun closestToSumNaive(a: IntArray, b: IntArray, target: Int): Pair<Int, Int> {
    var closest = Pair(a[0], b[0])
    for (i in a.indices) {
        for (j in b.indices) {
            if (abs(a[i] + b[j] - target) < abs(closest.first + closest.second - target)) {
                closest = Pair(a[i], b[j])
            }
        }
    }
    return closest
}

/**
 * we solve simplified version of the task:
 * find exact sum or return null
 *
 * brute force approach
 *
 * time: O(N^2)
 * space: O(1)
 */
fun closestToSumSimpleNaive(a: IntArray, b: IntArray, target: Int): Pair<Int, Int>? {
    for (i in a.indices) {
        for (j in b.indices) {
            if (a[i] + b[j] == target) {
                return Pair(a[i], b[j])
            }
        }
    }
    return null
}

/**
 * we solve simplified version of the task:
 * find exact sum or return null
 *
 * store all elements of [a] in a set, traverse [b] and find matching pair
 *
 * time: O(N)
 * space: O(N)
 */
fun closestToSumSimple(a: IntArray, b: IntArray, target: Int): Pair<Int, Int>? {
    val memory = a.toHashSet() // for loop

    for (j in b.indices) {
        if (memory.contains(target - b[j])) {
            return Pair(target - b[j], b[j])
        }
    }
    return null
}

/**
 * alternative memory approach
 * store all elements of [a] in a set,
 * traverse [b] and then traverse [target] in two directions to find matching pair
 *
 * time: O(N*target) - not good for big [target]
 * space: O(N)
 * will give Wrong Answer in situations where [target] is much lower than array values or negative
 */
fun closestToSumAlternative(a: IntArray, b: IntArray, target: Int): Pair<Int, Int> {
    val memory = a.toHashSet() // for loop

    for (k in target downTo -target) {
        for (j in b.indices) {
            println("$k $j ${b[j]}")
            if (memory.contains(k - b[j])) {
                return Pair(k - b[j], b[j])
            }
            if (memory.contains(k + b[j])) {
                return Pair(k + b[j], b[j])
            }
        }
    }
    return Pair(a[0], b[0])
}