package kz.ildar.sandbox.utils.leetcode.interview

import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigInteger

class LongestMountainKtTest {

    @Test
    fun longestMountainMain() {
        val input = intArrayOf(2, 1, 4, 7, 3, 2, 5)
        assertEquals(5, longestMountain(input))
    }

    @Test
    fun longestMountain1() {
        val input = intArrayOf(0, 1, 2, 3, 4, 5, 4, 3, 2, 1, 0)
        assertEquals(11, longestMountain(input))
    }

    @Test
    fun longestMountain2() {
        val input = intArrayOf(0, 1, 0)
        assertEquals(3, longestMountain(input))
    }

    @Test
    fun longestMountain3() {
        val input = intArrayOf(875, 884, 239, 731, 723, 685)
        assertEquals(4, longestMountain(input))
    }

    @Test
    fun longestMountainFail() {
        val input = intArrayOf(2, 2, 2)
        assertEquals(0, longestMountain(input))
    }

    @Test
    fun longestMountainFail2() {
        val input = intArrayOf(2, 1, 0)
        assertEquals(0, longestMountain(input))
    }

    @Test
    fun longestMountainFail3() {
        val input = intArrayOf(1, 2, 3)
        assertEquals(0, longestMountain(input))
    }

    @Test
    fun longestMountainFlat() {
        val input = intArrayOf(0, 2, 2)
        assertEquals(0, longestMountain(input))
    }

    @Test
    fun longestMountainFlat1() {
        val input = intArrayOf(0, 1, 2, 3, 2, 2)
        assertEquals(5, longestMountain(input))
    }

    @Test
    fun longestMountainFlat2() {
        val input = intArrayOf(0, 1, 2, 3, 2, 2, 3, 4, 5, 6, 7, 5, 2)
        assertEquals(8, longestMountain(input))
    }

    @Test
    fun testB() {
        assertEquals(BigInteger("5"), fib(5))
    }
}