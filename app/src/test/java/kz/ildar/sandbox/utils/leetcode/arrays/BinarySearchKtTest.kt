package kz.ildar.sandbox.utils.leetcode.arrays

import org.junit.Assert.*
import org.junit.Test
import java.lang.IllegalArgumentException
import kotlin.test.assertFailsWith

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
    fun testPositive() {
        assertEquals(0, binarySearch(intArrayOf(1, 4, 6, 15), 1))
        assertEquals(1, binarySearch(intArrayOf(1, 4, 6, 15), 4))
        assertEquals(2, binarySearch(intArrayOf(1, 4, 6, 15), 6))
        assertEquals(3, binarySearch(intArrayOf(1, 4, 6, 15), 15))
    }

    @Test
    fun testInvalidArray() {
        assertFailsWith<IllegalArgumentException> {
            binarySearch(intArrayOf(12, 16, 5), 12)
        }
    }
}