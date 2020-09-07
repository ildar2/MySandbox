package kz.ildar.sandbox.utils.leetcode

import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigInteger

class StaircaseKtTest {

    @Test
    fun testCornerCases() {
        assertEquals(BigInteger("1"), uniqueWaysToClimbStaircase(0))
        assertEquals(BigInteger("1"), uniqueWaysToClimbStaircase(1))
        assertEquals(BigInteger("2"), uniqueWaysToClimbStaircase(2))
    }

    @Test
    fun test3() {
        assertEquals(BigInteger("3"), uniqueWaysToClimbStaircase(3))
    }

    @Test
    fun testMain() {
        assertEquals(BigInteger("5"), uniqueWaysToClimbStaircase(4))
    }

    @Test
    fun test10() {
        assertEquals(BigInteger("89"), uniqueWaysToClimbStaircase(10))
    }

    @Test
    fun test100() {
        assertEquals(BigInteger("573147844013817084101"), uniqueWaysToClimbStaircase(100))
    }

    @Test
    fun testGeneralized_1_2() {
        assertEquals(1, uniqueWaysToClimbStaircaseGeneralized(1, setOf(1, 2)))
        assertEquals(2, uniqueWaysToClimbStaircaseGeneralized(2, setOf(1, 2)))
        assertEquals(3, uniqueWaysToClimbStaircaseGeneralized(3, setOf(1, 2)))
        assertEquals(5, uniqueWaysToClimbStaircaseGeneralized(4, setOf(1, 2)))
        assertEquals(8, uniqueWaysToClimbStaircaseGeneralized(5, setOf(1, 2)))
    }

    @Test
    fun testGeneralized_1_3_5() {
        assertEquals(1, uniqueWaysToClimbStaircaseGeneralized(1, setOf(1, 3, 5)))
        assertEquals(1, uniqueWaysToClimbStaircaseGeneralized(2, setOf(1, 3, 5)))
        assertEquals(2, uniqueWaysToClimbStaircaseGeneralized(3, setOf(1, 3, 5)))
        assertEquals(3, uniqueWaysToClimbStaircaseGeneralized(4, setOf(1, 3, 5)))
        assertEquals(5, uniqueWaysToClimbStaircaseGeneralized(5, setOf(1, 3, 5)))
        assertEquals(8, uniqueWaysToClimbStaircaseGeneralized(6, setOf(1, 3, 5)))
        assertEquals(12, uniqueWaysToClimbStaircaseGeneralized(7, setOf(1, 3, 5)))
        assertEquals(19, uniqueWaysToClimbStaircaseGeneralized(8, setOf(1, 3, 5)))
        assertEquals(30, uniqueWaysToClimbStaircaseGeneralized(9, setOf(1, 3, 5)))
        assertEquals(47, uniqueWaysToClimbStaircaseGeneralized(10, setOf(1, 3, 5)))
        assertEquals(74, uniqueWaysToClimbStaircaseGeneralized(11, setOf(1, 3, 5)))
        assertEquals(116, uniqueWaysToClimbStaircaseGeneralized(12, setOf(1, 3, 5)))
    }

    @Test
    fun testGeneralized_1_2_3() {
        assertEquals(1, uniqueWaysToClimbStaircaseGeneralized(1, setOf(1, 2, 3)))
        assertEquals(2, uniqueWaysToClimbStaircaseGeneralized(2, setOf(1, 2, 3)))
        assertEquals(4, uniqueWaysToClimbStaircaseGeneralized(3, setOf(1, 2, 3)))
        assertEquals(7, uniqueWaysToClimbStaircaseGeneralized(4, setOf(1, 2, 3)))
        assertEquals(13, uniqueWaysToClimbStaircaseGeneralized(5, setOf(1, 2, 3)))
        assertEquals(24, uniqueWaysToClimbStaircaseGeneralized(6, setOf(1, 2, 3)))
    }

    @Test
    fun testGeneralized_1_2_3_etalon() {
        assertEquals(1, countWays(1, 3))
        assertEquals(2, countWays(2, 3))
        assertEquals(4, countWays(3, 3))
        assertEquals(7, countWays(4, 3))
        assertEquals(13, countWays(5, 3))
        assertEquals(24, countWays(6, 3))
    }
}