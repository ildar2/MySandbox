package kz.ildar.sandbox.utils.mock

import org.junit.Test

import org.junit.Assert.*

class DiagTraverseKtTest {

    @Test
    fun diagTraverseTest1() {
        assertEquals(arrayListOf(1), diagTraverse(arrayOf(intArrayOf(1))))
    }

    @Test
    fun diagTraverseTest2() {
        assertEquals(arrayListOf(1, 3, 2, 4), diagTraverse(arrayOf(intArrayOf(1, 2), intArrayOf(3, 4))))
    }
}