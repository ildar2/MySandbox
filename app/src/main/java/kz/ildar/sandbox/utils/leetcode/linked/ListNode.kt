package kz.ildar.sandbox.utils.leetcode.linked

class ListNode(var `val`: Int) {

    var next: ListNode? = null

    override fun equals(other: Any?): Boolean = other is ListNode && other.`val` == `val`

    override fun hashCode(): Int = `val`

    override fun toString(): String = "ListNode $`val` [->${next?.`val`}]"

    fun print(): String = "$`val`, ${next?.print()}"
}