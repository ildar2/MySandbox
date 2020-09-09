package kz.ildar.sandbox.utils.leetcode.strings

/**
 * find out if [word] is an anagram of [this]
 *
 * input: triangle, integral
 * output: true
 *
 * input: abbc, accb
 * output: false
 */
fun String.isAnagram(word: String): Boolean {
    return isAnagramArray(word)
}

/**
 * store chars from [word] to list, then remove for each letter in [this]
 *
 * time: O(N*M)
 * space: O(N)
 */
private fun String.isAnagramNaive(word: String): Boolean {
    if (word.length != length) return false

    val list = word.toMutableList()//for loop

    this.forEach {
        if (!list.remove(it)) return false
    }

    return list.isEmpty()
}

/**
 * compare two sorted arrays of char
 *
 * time: O(N*logN)
 * space: O(N)
 */
private fun String.isAnagramSorting(word: String): Boolean = word.toCharArray().apply { sort() }
    .contentEquals(this.toCharArray().apply { sort() })

/**
 * count letters from both arrays then compare
 *
 * time: O(N)
 * space: O(1)
 */
private fun String.isAnagramArray(word: String): Boolean {
    if (word.length != length) return false

    val chars = IntArray(256)
    for (i in 0 until length) {
        chars[this[i].toInt()]++
        chars[word[i].toInt()]--
    }
    chars.forEach {
        if (it != 0) return false
    }
    return true
}