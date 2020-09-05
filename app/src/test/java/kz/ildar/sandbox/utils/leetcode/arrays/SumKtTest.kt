package kz.ildar.sandbox.utils.leetcode.arrays

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SumKtTest {

    @Test
    fun hasTwoNumbersTest() {
        assertTrue(hasTwoNumbers(intArrayOf(1, 2, 3), intArrayOf(10, 20, 30, 40), 42))
    }

    @Test
    fun hasTwoNumbersTest2() {
        assertTrue(hasTwoNumbers(intArrayOf(0, 0, -5, 30212), intArrayOf(-10, 40, -3, 9), -8))
    }

    @Test
    fun testFalse() {
        assertFalse(hasTwoNumbers(intArrayOf(1, 2, 3), intArrayOf(10, 20, 30, 40), 82))
    }
}