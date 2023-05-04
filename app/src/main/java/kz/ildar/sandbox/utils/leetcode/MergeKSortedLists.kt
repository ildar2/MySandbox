package kz.ildar.sandbox.utils.leetcode

import android.os.Build
import androidx.annotation.RequiresApi
import java.util.*

class ListNode(var `val`: Int) {
    var next: ListNode? = null
}

/**
 * Example:
 * var li = ListNode(5)
 * var v = li.`val`
 * Definition for singly-linked list.
 * class ListNode(var `val`: Int) {
 *     var next: ListNode? = null
 * }
 *
 * https://leetcode.com/problems/merge-k-sorted-lists/
 */
class SolutionMerge {
    @RequiresApi(Build.VERSION_CODES.N)
    fun mergeKLists(lists: Array<ListNode?>): ListNode? {
        val pq = PriorityQueue<ListNode> { a, b -> a.`val` - b.`val` }
        lists.filterNotNull().forEach { node -> pq.offer(node) }
        val dummy = ListNode(0)
        var current = dummy

        while (pq.isNotEmpty()) {
            val node = pq.poll()
            current.next = node
            current = current.next!!

            if (node.next != null) {
                pq.offer(node.next)
            }
        }

        return dummy.next
    }
}