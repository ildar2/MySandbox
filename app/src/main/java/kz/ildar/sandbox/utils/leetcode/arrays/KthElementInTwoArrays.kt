package kz.ildar.sandbox.utils.leetcode.arrays

/**
 * Given two sorted arrays of size m and n respectively,
 * you are tasked with finding the element that would be
 * at the k’th position of the final sorted array.
 * input: [2, 3, 6, 7, 9] [1, 4, 8, 10] k=5
 * output: 6
 */
fun kth(array1: IntArray, array2: IntArray, k: Int): Int {
    return kthDivideAndConquer(array1, array2, k)
}

/**
 * using merge technique we find k'th element consequently
 * time: O(n + m)
 * space: O(n + m)
 */
private fun kthWithMerging(array1: IntArray, array2: IntArray, k: Int): Int {
    val m = array1.size
    val n = array2.size
    val sorted = IntArray(m + n)
    var i = 0
    var j = 0
    var d = 0
    while(i < m && j < n) {
        if (array1[i] <= array2[j]) {
            sorted[d++] = array1[i++]
        } else {
            sorted[d++] = array2[j++]
        }
    }
    while(i < m)
        sorted[d++] = array1[i++]
    while(j < n)
        sorted[d++] = array2[j++]

    return sorted[k - 1]
}

/**
 * same as [kthWithMerging], but without extra space and less time complexity
 * time: O(k)
 * space: O(1)
 */
private fun kthWithMergingInPlace(array1: IntArray, array2: IntArray, k: Int): Int {
    val m = array1.size
    val n = array2.size
    var i = 0
    var j = 0
    var counter = 0
    //traverse until the end of either array
    while(i < m && j < n) {
        counter++
        if (array1[i] <= array2[j]) {
            if (counter == k) return array1[i]
            i++
        } else {
            if (counter == k) return array2[j]
            j++
        }
    }
    //check remaining array1
    while(i < m) {
        counter++
        if (counter == k) return array1[i]
        i++
    }
    //check remaining array2
    while(j < n) {
        counter++
        if (counter == k) return array2[j]
        j++
    }
    return -1
}

/**
 * divide arrays in two and check
 * time: O(log n + log m)
 */
private fun kthDivideAndConquer(array1: IntArray, array2: IntArray, k: Int): Int {

    /**
     * отбрасываем правую/левую половины от массивов, пока
     * один из них не опустеет, тогда мы возьмём из другого
     */
    fun kth(start1: Int, start2: Int, end1: Int, end2: Int, k: Int): Int {
        if (start1 == end1) return array2[k + start2]
        if (start2 == end2) return array1[k + start1]
        val mid1: Int = (end1 - start1) / 2
        val mid2: Int = (end2 - start2) / 2

        return if (mid1 + mid2 < k) {
            if (array1[start1 + mid1] > array2[start2 + mid2])
            //отбрасываем левую половину второго массива
                kth(start1, start2 + mid2 + 1, end1, end2, k - mid2 - 1)
            else
            //отбрасываем левую половину первого массива
                kth(start1 + mid1 + 1, start2, end1, end2, k - mid1 - 1)
        } else {
            if (array1[start1 + mid1] > array2[start2 + mid2])
            //отбрасываем правую половину первого массива
                kth(start1, start2, start1 + mid1, end2, k)
            else
            //отбрасываем правую половину второго массива
                kth(start1, start2, end1, start2 + mid2, k)
        }
    }

    return kth(0, 0, array1.size, array2.size, k - 1)
}
