package kz.ildar.sandbox.utils.leetcode.arrays

import org.junit.Assert.assertEquals
import org.junit.Test

class ClosestToSumKtTest {

    @Test
    fun testMain() {
        val a = intArrayOf(-1, 3, 8, 2, 9, 5)
        val b = intArrayOf(4, 1, 2, 10, 5, 20)
        val target = 24
        try {
            assertEquals(Pair(5, 20), closestToSum(a, b, target))
        } catch (e: AssertionError) {
            assertEquals(Pair(3, 20), closestToSum(a, b, target))
        }
    }

    @Test
    fun testSimplified() {
        val a = intArrayOf(-1, 3, 8, 2, 9, 5)
        val b = intArrayOf(4, 1, 2, 10, 5, 20)
        val target = 23
        assertEquals(Pair(3, 20), closestToSumSimple(a, b, target))
    }

    @Test
    fun testUpperLimit() {
        val a = intArrayOf(-1, 3, 8, 2, 9, 5)
        val b = intArrayOf(4, 1, 2, 10, 5, 20)
        val target = 50
        assertEquals(Pair(9, 20), closestToSum(a, b, target))
    }

    @Test
    fun testLowerLimit() {
        val a = intArrayOf(10, 30, 80, 20, 90, 50)
        val b = intArrayOf(40, 11, 20, 100, 50, 20)
        val target = 1
        assertEquals(Pair(10, 11), closestToSum(a, b, target))
    }

    @Test
    fun testSampleInput() {
        val a1 = intArrayOf(-1, 3, 8, 2, 9, 5)
        val a2 = intArrayOf(4, 1, 2, 10, 5, 20)
        val aTarget = 24
        try {
            assertEquals(Pair(5, 20), closestToSum(a1, a2, aTarget))
        } catch (e: AssertionError) {
            assertEquals(Pair(3, 20), closestToSum(a1, a2, aTarget))
        }

        val b1 = intArrayOf(7, 4, 1, 10)
        val b2 = intArrayOf(4, 5, 8, 7)
        val bTarget = 13
        try {
            assertEquals(Pair(4, 8), closestToSum(b1, b2, bTarget))
        } catch (e: AssertionError) {
            assertEquals(Pair(7, 7), closestToSum(b1, b2, bTarget))//{7, 5}, or {10, 4}
        }

        val c1 = intArrayOf(6, 8, -1, -8, -3)
        val c2 = intArrayOf(4, -6, 2, 9, -3)
        val cTarget = 3
        try {
            assertEquals(Pair(-1, 4), closestToSum(c1, c2, cTarget))
        } catch (e: AssertionError) {
            assertEquals(Pair(6, -3), closestToSum(c1, c2, cTarget))
        }

        val d1 = intArrayOf(19, 14, 6, 11, -16, 14, -16, -9, 16, 13)
        val d2 = intArrayOf(13, 9, -15, -2, -18, 16, 17, 2, -11, -7)
        val dTarget = -15
        try {
            assertEquals(Pair(-16, 2), closestToSum(d1, d2, dTarget))
        } catch (e: AssertionError) {
            assertEquals(Pair(-9, -7), closestToSum(d1, d2, dTarget))
        }
    }
}