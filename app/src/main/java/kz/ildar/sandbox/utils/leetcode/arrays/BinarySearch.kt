package kz.ildar.sandbox.utils.leetcode.arrays

const val NO_VALUE = -1

/**
 * find index of [value] in a sorted [array]
 * performs with O(logN), but
 * can include O(N) validation to check if array is sorted
 * mb can validate as the search goes
 */
fun binarySearch(array: IntArray, value: Int, validate: Boolean = false): Int {
    if (validate && !checkSorted(array)) return NO_VALUE

    var lo = 0
    var hi = array.lastIndex
    while (lo <= hi) {
        val mid = lo + (hi - lo) / 2
        when {
            value > array[mid] -> lo = mid + 1
            value < array[mid] -> hi = mid - 1
            else -> return mid
        }
    }
    return NO_VALUE
}

/**
 * check if [array] is sorted
 * currently throws, but can return false
 * performs with O(N)
 */
private fun checkSorted(array: IntArray): Boolean {
    if (array.isEmpty()) return false

    for (i in 1 until array.size) {
        if (array[i] < array[i - 1]) throw IllegalArgumentException("array is not sorted")
    }
    return true
}
