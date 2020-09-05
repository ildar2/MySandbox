package kz.ildar.sandbox.utils.leetcode.arrays

import org.junit.Assert.assertEquals
import org.junit.Test

class FirstDuplicateKtTest {

    @Test
    fun invalid() {
        assertEquals(-1, firstDuplicate(intArrayOf(0, 12, 15)))
        assertEquals(-1, firstDuplicate(intArrayOf(1, 2, 4)))
    }

    @Test
    fun test1() {
        assertEquals(1, firstDuplicate(intArrayOf(1, 2, 1, 2, 3, 3)))
    }

    @Test
    fun test2() {
        assertEquals(3, firstDuplicate(intArrayOf(2, 1, 3, 5, 3, 2)))
    }

    @Test
    fun test3() {
        assertEquals(0, firstDuplicate(intArrayOf(1, 2, 3, 4, 5, 6)))
    }

    @Test
    fun test4() {
        assertEquals(6, firstDuplicate(intArrayOf(6, 6, 1, 5, 3, 2)))
    }
}