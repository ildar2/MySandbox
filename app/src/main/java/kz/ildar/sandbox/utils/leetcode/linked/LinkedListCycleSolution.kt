package kz.ildar.sandbox.utils.leetcode.linked

/**
 * https://leetcode.com/problems/linked-list-cycle/
 */
class LinkedListCycleSolution {
    fun hasCycle(head: ListNode?): Boolean {
        var slow = head
        var fast = head?.next
        while (slow != null) {
            if (slow == fast) return true
            slow = slow.next
            fast = fast?.next?.next
        }
        return false
    }
}