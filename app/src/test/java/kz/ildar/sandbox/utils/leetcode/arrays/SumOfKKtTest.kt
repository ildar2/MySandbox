package kz.ildar.sandbox.utils.leetcode.arrays

import org.junit.Assert.*
import org.junit.Test

class SumOfKKtTest {

    @Test
    fun test1() {
        assertEquals(39, sumOfK(intArrayOf(1, 4, 2, 10, 23, 3, 1, 0, 20), 4))
    }

    @Test
    fun test2() {
        assertEquals(700, sumOfK(intArrayOf(100, 200, 300, 400), 2))
    }

    @Test
    fun test3() {
        assertEquals(-1, sumOfK(intArrayOf(2, 3), 3))
    }
}