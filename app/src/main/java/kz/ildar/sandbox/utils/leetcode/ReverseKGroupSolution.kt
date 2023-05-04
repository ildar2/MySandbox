package kz.ildar.sandbox.utils.leetcode

class ReverseKGroupSolution {
    /**
     * Given the head of a linked list, reverse the nodes of the list k at a time,
     * and return the modified list. k is a positive integer and is less than or
     * equal to the length of the linked list. If the number of nodes is not a multiple
     * of k then left-out nodes, in the end, should remain as it is.
     * You may not alter the values in the list's nodes, only nodes themselves may be changed.
     *
     * https://leetcode.com/problems/reverse-nodes-in-k-group/
     */
    fun reverseKGroup(head: ListNode?, k: Int): ListNode? {
        if (head == null || k == 1) return head

        val dummy = ListNode(0)
        dummy.next = head
        var pre: ListNode? = dummy
        var end: ListNode? = dummy

        while (end?.next != null) {
            // move end pointer to the kth node
            for (i in 0 until k) {
                end = end?.next
                if (end == null) {
                    return dummy.next
                }
            }

            val start = pre?.next
            val next = end?.next
            end?.next = null
            pre?.next = reverse(start)
            start?.next = next
            pre = start
            end = pre
        }

        var count = 0
        var curr = dummy.next
        while (curr != null) {
            curr = curr.next
            count++
        }
        if (count < k) {
            pre?.next = reverse(pre?.next)
        }
        return dummy.next
    }

    private fun reverse(head: ListNode?): ListNode? {
        var curr = head
        var prev: ListNode? = null
        while (curr != null) {
            val next = curr.next
            curr.next = prev
            prev = curr
            curr = next
        }
        return prev
    }
}