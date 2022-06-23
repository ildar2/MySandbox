package kz.ildar.sandbox.utils.leetcode.linked

/**
 * You are given the heads of two sorted linked lists list1 and list2.
 * Merge the two lists in a one sorted list. The list should be made by splicing together the nodes of the first two lists.
 * Return the head of the merged linked list.
 *
 * https://leetcode.com/problems/merge-two-sorted-lists/
 */
class MergeTwoSortedListsSolution {

    /**
     * Input: list1 = [1,2,4], list2 = [1,3,4]
     * Output: [1,1,2,3,4,4]
     */
    fun mergeTwoLists(list1: ListNode?, list2: ListNode?): ListNode? {
        return mergeRecursive(list1, list2)
    }

    private fun mergeRecursive(
        list1: ListNode?,
        list2: ListNode?,
    ): ListNode? = when {
        list1 == null -> list2
        list2 == null -> list1
        list1.`val` <= list2.`val` -> {
            list1.next = mergeRecursive(list1.next, list2)
            list1
        }
        else -> {
            list2.next = mergeRecursive(list1, list2.next)
            list2
        }
    }

    private fun mergeIterative(
        list1: ListNode?,
        list2: ListNode?
    ): ListNode? {
        var cur1 = list1
        var cur2 = list2
        var resHead: ListNode? = null
        var res: ListNode? = null

        var recCounter = 0

        while (recCounter < 30 && (cur1 != null || cur2 != null)) {
            if (cur1?.`val` ?: Int.MAX_VALUE >= cur2?.`val` ?: Int.MAX_VALUE) {
                if (resHead == null) {
                    resHead = cur2
                    res = cur2
                } else {
                    res?.next = cur2
                    res = cur2
                }
                cur2 = cur2?.next
            } else {
                if (resHead == null) {
                    resHead = cur1
                    res = cur1
                } else {
                    res?.next = cur1
                    res = cur1
                }
                cur1 = cur1?.next
            }
            recCounter++
        }
        println("result: ${resHead?.print()}")

        return resHead
    }
}