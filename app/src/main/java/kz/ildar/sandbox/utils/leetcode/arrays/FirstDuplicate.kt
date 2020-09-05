package kz.ildar.sandbox.utils.leetcode.arrays

import kotlin.math.abs
import kotlin.math.min

const val INVALID_INPUT = -1
const val NO_DUPLICATES = 0

/**
 * given [array]
 * numbers in [array] are 1 to [array.size]
 * return first duplicate or 0
 *
 * input: [1, 2, 1, 2, 3, 3]
 * output: 1
 */
fun firstDuplicate(array: IntArray): Int {
    //validate
    for (item in array) {
        if (item <= 0 || item > array.size) {
            return INVALID_INPUT
        }
    }
    return firstDuplicateOptimal(array)
}

/**
 * treat values as indexes
 * check if already visited (visited -> negative)
 * time: O(N)
 * space: O(1)
 * mutates array
 */
private fun firstDuplicateOptimal(array: IntArray): Int {
    for (i in array.indices) {
        if (array[abs(array[i]) - 1] < 0) {
            return abs(array[i])
        } else {
            array[abs(array[i]) - 1] = -array[abs(array[i]) - 1]
        }
    }
    return NO_DUPLICATES
}

/**
 * naive approach: check duplicates for each value, then find lowest index
 * time: O(N^2)
 * space: O(1)
 */
private fun firstDuplicateTime(array: IntArray): Int {
    var duplicateIndex = array.size
    for (i in array.indices) {
        for (j in (i + 1) until array.size) {
            if (array[i] == array[j]) {
                duplicateIndex = min(duplicateIndex, j)
            }
        }
    }
    if (duplicateIndex == array.size) return NO_DUPLICATES
    return array[duplicateIndex]
}

/**
 * save all values to memory
 * if it's there, we've got a duplicate
 * time: O(N)
 * space: O(N)
 */
private fun firstDuplicateSpace(array: IntArray): Int {
    val memory = HashSet<Int>()
    for (item in array) {
        if (memory.contains(item)) return item
        memory.add(item)
    }
    return NO_DUPLICATES
}
