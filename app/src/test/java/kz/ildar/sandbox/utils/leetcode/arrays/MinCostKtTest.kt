package kz.ildar.sandbox.utils.leetcode.arrays

import org.junit.Assert.assertEquals
import org.junit.Test

class MinCostKtTest {

    @Test
    fun test1() {
        assertEquals(7, minCost(intArrayOf(1, 2, 3, 4, 6), 3))
    }

    @Test
    fun test2() {
        assertEquals(21, minCost(intArrayOf(2, 3, 4, 4, 1, 7, 6), 4))
    }
}