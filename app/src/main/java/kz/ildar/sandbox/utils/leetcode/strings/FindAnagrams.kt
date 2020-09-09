package kz.ildar.sandbox.utils.leetcode.strings

import kotlin.math.min

/**
 * find all the start indices of [p]'s anagrams in [s].
 *
 *
 */
fun findAnagrams(s: String, p: String): List<Int> {
    return findAnagramsOptimal(s, p)

}

/**
 *
 */
fun findAnagramsNaive(s: String, p: String): List<Int> {
    val ans = mutableListOf<Int>()

    for (i in 0..s.length - p.length) {
        if (s.substring(i, i + p.length).isAnagram(p)) {
            ans.add(i)
        }
    }

    return ans
}

/**
 * Sliding window technique
 *
 *
 */
fun findAnagramsOptimal(s: String, p: String): List<Int> {
    val ans = mutableListOf<Int>()

    /**
     * O(1) anagram check
     */
    fun check(sF: IntArray, pF: IntArray): Boolean {
        for (i in 0 until 26) {
            if (sF[i] != pF[i]) return false
        }
        return true
    }

    var l = 0
    var r = 0

    val sFreq = IntArray(26)
    val pFreq = IntArray(26)

    for (i in p.indices) {
        pFreq[p[i] - 'a']++
    }
    for (i in 0 until min(s.length, p.length)) {
        sFreq[s[i] - 'a']++
        r++
    }

    while(r < s.length) {
        if (check(sFreq, pFreq)) ans.add(l)
        sFreq[s[l] - 'a']--
        sFreq[s[r] - 'a']++
        l++
        r++
    }
    if (check(sFreq, pFreq)) ans.add(l)

    return ans
}

fun findAnagramsMap(s: String?, p: String?): List<Int?>? {
    val res: MutableList<Int?> = ArrayList()
    if (s == null || p == null || s.length < p.length) return res
    val map: HashMap<Char, Int?> = HashMap()
    for (c in p.toCharArray()) {
        map[c] = map[c] ?: 0 + 1
    }
    var left = 0
    var right = 0
    val len = p.length
    var count = len // record the whole length of the string p need to match
    while(right < s.length) {
        val rightC = s[right]
        if (map.containsKey(rightC)) {
            map[rightC] = map[rightC]!! - 1 // map record the difference to the p
            if (map[rightC]!! >= 0) count--
        }
        if (right - left == len - 1) {
            val leftC = s[left]
            if (count == 0) {  // clear:  全部清零 符合条件
                res.add(left)
            }

            //  Start reset variable when left moving forward.
            if (map.containsKey(leftC)) {
                // Check whether leftC took a role in the  s[left, right]
                // If map.get(leftC) is negative, means  s[left, right] has too many leftC than string p.  so count won't change. otherwise count should be change.
                // 这里就是， 判断是否leftC 是是否在 , p 和 现在的 s[left, right] 字符串的diff 里面是否起到作用
                // 如果 map.get(leftC) 是 负的， 说明 s[left, right] 里面有了太多的 leftC, 去掉一个leftC 不会造成对于 count  的影响. 反之就要放回一个difference: count++;
                if (map[leftC]!! >= 0) {
                    count++
                }
                map[leftC] = map[leftC]!! + 1
            }
            left++
        }
        right++
    }
    return res
}