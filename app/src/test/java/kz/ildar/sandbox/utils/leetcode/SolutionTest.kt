package kz.ildar.sandbox.utils.leetcode

import org.junit.Test

import org.junit.Assert.*

class SolutionTest {

    private val solution = Solution()

    @Test
    fun numToRoman999() {
        assertEquals("CMXCIX", solution.numToRoman(999))
    }

    @Test
    fun numToRoman1904() {
        assertEquals("MCMIV", solution.numToRoman(1904))
    }

    @Test
    fun numToRoman94() {
        assertEquals("XCIV", solution.numToRoman(94))
    }

    @Test
    fun numToRoman99() {
        assertEquals("XCIX", solution.numToRoman(99))
    }

    @Test
    fun numToRoman0() {
        assertEquals("", solution.numToRoman(0))
    }

    @Test
    fun numToRoman1() {
        assertEquals("I", solution.numToRoman(1))
    }

    @Test
    fun numToRoman2() {
        assertEquals("II", solution.numToRoman(2))
    }

    @Test
    fun numToRoman3() {
        assertEquals("III", solution.numToRoman(3))
    }

    @Test
    fun numToRoman4() {
        assertEquals("IV", solution.numToRoman(4))
    }

    @Test
    fun numToRoman5() {
        assertEquals("V", solution.numToRoman(5))
    }

    @Test
    fun numToRoman6() {
        assertEquals("VI", solution.numToRoman(6))
    }

    @Test
    fun numToRoman7() {
        assertEquals("VII", solution.numToRoman(7))
    }

    @Test
    fun numToRoman8() {
        assertEquals("VIII", solution.numToRoman(8))
    }

    @Test
    fun numToRoman9() {
        assertEquals("IX", solution.numToRoman(9))
    }

    @Test
    fun numToRoman10() {
        assertEquals("X", solution.numToRoman(10))
    }

    @Test
    fun numToRoman11() {
        assertEquals("XI", solution.numToRoman(11))
    }
}