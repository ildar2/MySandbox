package kz.ildar.sandbox.utils.leetcode.strings

import org.junit.Assert.*
import org.junit.Test

class IsAnagramKtTest {

    @Test
    fun testPositive() {
        assertTrue("anagram".isAnagram("granama"))
        assertTrue("abc".isAnagram("acb"))
        assertTrue("abc".isAnagram("bac"))
        assertTrue("abc".isAnagram("bca"))
        assertTrue("abc".isAnagram("cba"))
        assertTrue("abc".isAnagram("cab"))
        assertTrue("".isAnagram(""))
    }

    @Test
    fun testNegative() {
        assertFalse("abc".isAnagram("abca"))
        assertFalse("abca".isAnagram("abc"))
        assertFalse("abca".isAnagram("abbc"))
        assertFalse("".isAnagram(" "))
    }
}