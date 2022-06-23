package kz.ildar.sandbox.utils.leetcode.linked

import org.junit.Assert.assertEquals
import org.junit.Test

class RemoveNthFromEndSolutionTest {
    /**
     * Input: head = [1,2,3,4,5], n = 2
     * Output: [1,2,3,5]
     */
    @Test
    fun example1() {
        val node1 = ListNode(1)
        val node2 = ListNode(2)
        val node3 = ListNode(3)
        val node4 = ListNode(4)
        val node5 = ListNode(5)
        node1.next = node2
        node2.next = node3
        node3.next = node4
        node4.next = node5
        val newHead = RemoveNthFromEndSolution().removeNthFromEnd(node1, 2)
        assertEquals(node1, newHead)
        assertEquals(node2, newHead?.next)
        assertEquals(node3, newHead?.next?.next)
        assertEquals(node5, newHead?.next?.next?.next)
        assertEquals(null, newHead?.next?.next?.next?.next)
    }

    /**
     * Input: head = [1], n = 1
     * Output: []
     */
    @Test
    fun example2() {
        val node1 = ListNode(1)
        val newHead = RemoveNthFromEndSolution().removeNthFromEnd(node1, 1)
        assertEquals(null, newHead)
    }

    /**
     * Input: head = [1,2], n = 1
     * Output: [1]
     */
    @Test
    fun example3() {
        val node1 = ListNode(1)
        val node2 = ListNode(2)
        node1.next = node2
        val newHead = RemoveNthFromEndSolution().removeNthFromEnd(node1, 1)
        assertEquals(node1, newHead)
        assertEquals(null, newHead?.next)
    }

    /**
     * Input: head = [], n = 1
     * Output: []
     */
    @Test
    fun testEmpty() {
        val node1: ListNode? = null
        val newHead = RemoveNthFromEndSolution().removeNthFromEnd(node1, 1)
        assertEquals(null, newHead)
    }

    /**
     * Input: head = [1,2], n = 3
     * Output: [1,2]
     */
    @Test
    fun testExceeding() {
        val node1 = ListNode(1)
        val node2 = ListNode(2)
        node1.next = node2
        val newHead = RemoveNthFromEndSolution().removeNthFromEnd(node1, 3)
        assertEquals(node1, newHead)
        assertEquals(node2, newHead?.next)
        assertEquals(null, newHead?.next?.next)
    }
}