package kz.ildar.sandbox.utils.leetcode.arrays

import org.junit.Assert.*
import org.junit.Test
import java.lang.IllegalArgumentException

class BinarySearchKtTest {

    @Test
    fun testNegative() {
        assertEquals(NO_VALUE, binarySearch(intArrayOf(1, 4, 6, 15), 0))
        assertEquals(NO_VALUE, binarySearch(intArrayOf(1, 4, 6, 15), 3))
        assertEquals(NO_VALUE, binarySearch(intArrayOf(1, 4, 6, 15), 5))
        assertEquals(NO_VALUE, binarySearch(intArrayOf(1, 4, 6, 15), 8))
        assertEquals(NO_VALUE, binarySearch(intArrayOf(1, 4, 6, 15), 20))
    }

    @Test
    fun testPositiveEven() {
        assertEquals(0, binarySearch(intArrayOf(1, 4, 6, 15), 1))
        assertEquals(1, binarySearch(intArrayOf(1, 4, 6, 15), 4))
        assertEquals(2, binarySearch(intArrayOf(1, 4, 6, 15), 6))
        assertEquals(3, binarySearch(intArrayOf(1, 4, 6, 15), 15))
    }

    @Test
    fun testPositiveOdd() {
        assertEquals(0, binarySearch(intArrayOf(1, 4, 6, 15, 22), 1))
        assertEquals(1, binarySearch(intArrayOf(1, 4, 6, 15, 22), 4))
        assertEquals(2, binarySearch(intArrayOf(1, 4, 6, 15, 22), 6))
        assertEquals(3, binarySearch(intArrayOf(1, 4, 6, 15, 22), 15))
        assertEquals(4, binarySearch(intArrayOf(1, 4, 6, 15, 22), 22))
    }

    @Test
    fun testInvalidArray() {
//        assertFailsWith<IllegalArgumentException> {
//            binarySearch(intArrayOf(12, 16, 5), 12, true)
//        }
    }
}