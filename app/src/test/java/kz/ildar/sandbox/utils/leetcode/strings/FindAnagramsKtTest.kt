package kz.ildar.sandbox.utils.leetcode.strings

import org.junit.Assert.*
import org.junit.Test

class FindAnagramsKtTest {

    @Test
    fun testMain() {
        assertEquals(listOf(0, 1, 2), findAnagrams("abab", "ab"))
        assertEquals(listOf(0, 6), findAnagrams("cbaebabacd", "abc"))
        assertEquals(listOf<Int>(), findAnagrams("", "a"))
    }
}