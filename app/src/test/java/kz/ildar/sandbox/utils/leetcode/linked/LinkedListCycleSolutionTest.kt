package kz.ildar.sandbox.utils.leetcode.linked

import org.junit.Assert.*
import org.junit.Test

class LinkedListCycleSolutionTest {
    /**
     * Input: head = [1,2,3,4,5], pos = 2
     * Output: true
     * Explanation: There is a cycle in the linked list, where the tail connects to the 3rd node (2-indexed).
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
        node5.next = node3
        assertTrue(LinkedListCycleSolution().hasCycle(node1))
    }

    /**
     * Input: head = [1,2,3,4,5], pos = -1
     * Output: false
     * Explanation: There is no cycle in the linked list
     */
    @Test
    fun example2() {
        val node1 = ListNode(1)
        val node2 = ListNode(2)
        val node3 = ListNode(3)
        val node4 = ListNode(4)
        val node5 = ListNode(5)
        node1.next = node2
        node2.next = node3
        node3.next = node4
        node4.next = node5
        assertFalse(LinkedListCycleSolution().hasCycle(node1))
    }

    /**
     * Input: head = [], pos = -1
     * Output: false
     * Explanation: There is no cycle in an empty linked list
     */
    @Test
    fun testEmpty() {
        assertFalse(LinkedListCycleSolution().hasCycle(null))
    }

    @Test
    fun selfCycle() {
        val node1 = ListNode(1)
        node1.next = node1
        assertTrue(LinkedListCycleSolution().hasCycle(node1))
    }
}