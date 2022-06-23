package kz.ildar.sandbox.utils.leetcode.linked

/**
 * https://leetcode.com/problems/reverse-linked-list/
 */
class ReverseLinkedListSolution {
    fun reverseList(head: ListNode?): ListNode? {
        var pPrev: ListNode? = null
        var pCurr = head
        var pNext = head?.next
        while (pCurr != null) {
            pCurr.next = pPrev
            pPrev = pCurr
            pCurr = pNext
            pNext = pCurr?.next
        }
        return pPrev
    }
}

