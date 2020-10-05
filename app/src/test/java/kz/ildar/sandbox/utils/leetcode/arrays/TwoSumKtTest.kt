package kz.ildar.sandbox.utils.leetcode.arrays

import org.junit.Assert.assertArrayEquals
import org.junit.Test

class TwoSumKtTest {

    @Test
    fun test1() {
        assertArrayEquals(intArrayOf(0, 1), twoSum(intArrayOf(2, 7, 11, 15), 9))
    }

    @Test
    fun test2() {
        assertArrayEquals(intArrayOf(1, 2), twoSum(intArrayOf(3, 2, 4), 6))
    }

    @Test
    fun test3() {
        assertArrayEquals(intArrayOf(0, 1), twoSum(intArrayOf(3, 3), 6))
    }
}