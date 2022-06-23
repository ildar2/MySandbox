package kz.ildar.sandbox.utils.leetcode.linked

import org.junit.Assert.*
import org.junit.Test

class ReverseLinkedListSolutionTest {
    /**
     * Input: head = [1,2,3,4,5]
     * Output: [5,4,3,2,1]
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
        val newHead = ReverseLinkedListSolution().reverseList(node1)
        assertEquals(node5, newHead)
        assertEquals(node4, newHead?.next)
        assertEquals(node3, newHead?.next?.next)
        assertEquals(node2, newHead?.next?.next?.next)
        assertEquals(node1, newHead?.next?.next?.next?.next)
        assertEquals(null, newHead?.next?.next?.next?.next?.next)
    }

    /**
     * Input: head = [1,2]
     * Output: [2,1]
     */
    @Test
    fun example2() {
        val node1 = ListNode(1)
        val node2 = ListNode(2)
        node1.next = node2
        val newHead = ReverseLinkedListSolution().reverseList(node1)
        assertEquals(node2, newHead)
        assertEquals(node1, newHead?.next)
        assertEquals(null, newHead?.next?.next)
    }

    /**
     * Input: head = []
     * Output: []
     */
    @Test
    fun example3() {
        val node1 = null
        val newHead = ReverseLinkedListSolution().reverseList(node1)
        assertEquals(node1, newHead)
    }
}