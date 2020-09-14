package kz.ildar.sandbox.utils.leetcode.arrays

const val NO_VALUE = -1

/**
 * find index of [value] in a sorted [array]
 */
fun binarySearch(array: IntArray, value: Int): Int {
    if (array.isEmpty()) return NO_VALUE
    // validation
    val n = array.size
    for (i in 1 until n) {
        if (array[i] < array[i - 1]) throw IllegalArgumentException("array is not sorted")
    }
    var lo = 0
    var hi = n - 1
    while(lo <= hi) {
        val mid = lo + (hi - lo) / 2
        when {
            value > array[mid] -> lo = mid + 1
            value < array[mid] -> hi = mid - 1
            else -> return mid
        }
    }
    return NO_VALUE
}
