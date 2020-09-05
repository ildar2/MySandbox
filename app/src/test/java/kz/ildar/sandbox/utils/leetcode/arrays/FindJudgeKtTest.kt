package kz.ildar.sandbox.utils.leetcode.arrays

import org.junit.Assert.assertEquals
import org.junit.Test

class FindJudgeKtTest {
    @Test
    fun testOne() {
        assertEquals(1, findJudge(1, arrayOf()))
    }

    @Test
    fun test1() {
        assertEquals(2, findJudge(2, arrayOf(intArrayOf(1, 2))))
    }

    @Test
    fun test2() {
        assertEquals(3, findJudge(3, arrayOf(intArrayOf(1, 3), intArrayOf(2, 3))))
    }

    @Test
    fun test3() {
        assertEquals(NO_JUDGE, findJudge(3, arrayOf(intArrayOf(1, 3), intArrayOf(2, 3), intArrayOf(3, 1))))
    }

    @Test
    fun test4() {
        assertEquals(NO_JUDGE, findJudge(3, arrayOf(intArrayOf(1, 2), intArrayOf(2, 3))))
    }

    @Test
    fun test5() {
        assertEquals(3, findJudge(4, arrayOf(intArrayOf(1, 3), intArrayOf(1, 4), intArrayOf(2, 3), intArrayOf(2, 4), intArrayOf(4, 3))))
    }
}