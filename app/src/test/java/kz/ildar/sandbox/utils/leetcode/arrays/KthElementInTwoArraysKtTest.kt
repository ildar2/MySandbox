package kz.ildar.sandbox.utils.leetcode.arrays

import org.junit.Test

import org.junit.Assert.*

class KthElementInTwoArraysTest {

    @Test
    fun test1() {
        assertEquals(6, kth(intArrayOf(2, 3, 6, 7, 9), intArrayOf(1, 4, 8, 10), 5))
    }

    @Test
    fun test2() {
        assertEquals(5, kth(intArrayOf(1, 2, 3), intArrayOf(4, 5, 6), 5))
    }

    @Test
    fun test3() {
        assertEquals(5, kth(intArrayOf(4, 5, 6), intArrayOf(1, 2, 3), 5))
    }

    @Test
    fun test4() {
        assertEquals(4, kth(intArrayOf(4, 5, 6), intArrayOf(4, 5, 6), 1))
    }
}