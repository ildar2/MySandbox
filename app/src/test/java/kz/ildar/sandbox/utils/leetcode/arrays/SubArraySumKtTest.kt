package kz.ildar.sandbox.utils.leetcode.arrays

import org.junit.Assert.assertEquals
import org.junit.Test

class SubArraySumKtTest {

    @Test
    fun testMain() {
        assertEquals(Pair(1, 3), contiguousSubArraySum(intArrayOf(1, 2, 3, 7, 5), 12))
    }
}