package kz.ildar.sandbox.utils.leetcode.linked

/**
 * https://leetcode.com/problems/remove-nth-node-from-end-of-list/
 */
class RemoveNthFromEndSolution {
    /**
     * Input: head = [1,2,3,4,5], n = 2
     * Output: [1,2,3,5]
     */
    fun removeNthFromEnd(head: ListNode?, n: Int): ListNode? {
        return removeRecursive(head, n)
    }

    private fun removeRecursive(head: ListNode?, n: Int): ListNode? {
        val index = auxRemove(head, n)
        return if (index == n) head?.next else head
    }

    private fun auxRemove(head: ListNode?, n: Int): Int {
        if (head == null) return 0
        if (head.next == null) return 1
        val index = auxRemove(head.next, n)
        if (index == n) head.next = head.next?.next
        return index + 1
    }
}