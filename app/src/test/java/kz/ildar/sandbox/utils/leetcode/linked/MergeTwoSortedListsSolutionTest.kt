package kz.ildar.sandbox.utils.leetcode.linked

import org.junit.Assert.assertEquals
import org.junit.Test

class MergeTwoSortedListsSolutionTest {

    /**
     * Input: list1 = [1,2,4], list2 = [1,3,4]
     * Output: [1,1,2,3,4,4]
     */
    @Test
    fun example1() {
        val node1 = ListNode(1)
        val node2 = ListNode(2)
        val node3 = ListNode(4)
        node1.next = node2
        node2.next = node3

        val node4 = ListNode(1)
        val node5 = ListNode(3)
        val node6 = ListNode(4)
        node4.next = node5
        node5.next = node6
        val result = MergeTwoSortedListsSolution().mergeTwoLists(node1, node4)
        assertEquals(node1, result)
        assertEquals(node4, result?.next)
        assertEquals(node2, result?.next?.next)
        assertEquals(node5, result?.next?.next?.next)
        assertEquals(node3, result?.next?.next?.next?.next)
        assertEquals(node6, result?.next?.next?.next?.next?.next)
        assertEquals(null, result?.next?.next?.next?.next?.next?.next)
    }

    /**
     * Input: list1 = [], list2 = []
     * Output: []
     */
    @Test
    fun example2() {
        val node1: ListNode? = null
        val node2: ListNode? = null

        val result = MergeTwoSortedListsSolution().mergeTwoLists(node1, node2)
        assertEquals(null, result)
    }

    /**
     * Input: list1 = [], list2 = [0]
     * Output: [0]
     */
    @Test
    fun example3() {
        val node1: ListNode? = null
        val node2 = ListNode(0)

        val result = MergeTwoSortedListsSolution().mergeTwoLists(node1, node2)
        assertEquals(node2, result)
        assertEquals(null, result?.next)
    }

    /**
     * Input: list1 = [], list2 = [0]
     * Output: [0]
     */
    @Test
    fun fail1() {
        val node1 = ListNode(1)
        val node2: ListNode? = null

        val result = MergeTwoSortedListsSolution().mergeTwoLists(node1, node2)
        assertEquals(node1, result)
        assertEquals(null, result?.next)
    }
}