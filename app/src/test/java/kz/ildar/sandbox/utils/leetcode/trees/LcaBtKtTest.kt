package kz.ildar.sandbox.utils.leetcode.trees

import org.junit.Test

import org.junit.Assert.*

class LcaBtKtTest {

    @Test
    fun lcaBtTest() {
        val left2 = Node(5)
        val right2 = Node(8)
        val left1 = Node(6, left2, right2)
        val right1 = Node(14)
        val root = Node(10, left1, right1)
        assertEquals(root, lcaBt(root, left1, right1))
        assertEquals(Node(10), lcaBt(root, left1, right1))
        assertEquals(root, lcaBt(root, left2, right1))
        assertEquals(left1, lcaBt(root, left2, right2))
        assertEquals(left1, lcaBt(root, left1, right2))
    }
}