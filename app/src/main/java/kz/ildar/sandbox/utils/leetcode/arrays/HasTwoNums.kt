package kz.ildar.sandbox.utils.leetcode.arrays

/**
 * given two arrays [a], [b] each of length N, and a [target]
 * find out if there are two numbers (each from a different array)
 * that add up to [target]
 *
 * a: [1, 2, 3]
 * b: [10, 20, 30, 40]
 * target: 42
 * return: true
 */
fun hasTwoNumbers(a: IntArray, b: IntArray, target: Int): Boolean {
    return hasSumNaive(a, b, target)
}

/**
 * traverse [b] for each value of [a] to find matches
 * time: O(N*2)
 * space: O(1)
 * does not modify input
 */
private fun hasSumNaive(a: IntArray, b: IntArray, target: Int): Boolean {
    for (i in a.indices) {
        for (j in b.indices) {
            if (a[i] + b[j] == target) return true
        }
    }
    return false
}

/**
 * store [a] in hashset, find item in [b] that adds up to [target]
 * time: O(N)
 * space: O(N)
 * does not modify input
 */
private fun hasSumWithMemory(a: IntArray, b: IntArray, target: Int): Boolean {
    val memory = HashSet<Int>()
    for (item in a) {
        memory.add(item)
    }
    for (item in b) {
        if (memory.contains(target - item)) return true
    }
    return false
}

/**
 * We form a 2d matrix of sorted sums (lazily calculated)
 * and traverse it diagonally from right-top
 * each time going towards the [target]
 *
 * a: [1, 2, 3]
 * b: [10, 20, 30, 40]
 * target: 42
 *
 *   10  20  30  40 [b]
 * 1 11  21  31  41
 * 2 12  22  32  42
 * 3 13  23  33  43
 *[a]
 *
 * time: O(N*log(N))
 * space: O(1)
 * modifies input
 */
private fun hasSumTraversal(a: IntArray, b: IntArray, target: Int): Boolean {
    a.sort()
    b.sort()

    var i = 0//a
    var j = b.size - 1//b
    while(i < a.size && j >= 0) {
        val sum = a[i] + b[j]
        if (sum == target) return true
        if (sum < target) i++
        else j--
    }
    return false
}
