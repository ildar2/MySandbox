package kz.ildar.sandbox.utils.coursera.union_find

import java.lang.IllegalArgumentException

/**
 * Question 1
 *
 * n members
 * m log records of union with a timestamp
 * determine the time when all members are connected
 *
 * @param members - number of all friends with names from 0 to n-1
 * @param log - records of friendship, assuming sorted by timestamp
 * @return first timestamp when all people are connected
 */
fun question1(members: Int, log: List<Connection>): Long {
    val uf = FriendBookUnionFind(members)
    log.forEach {
        uf.union(it.p, it.q)
        if (uf.allConnected()) return it.timestamp
    }
    return -1//not enough connections
}

/**
 * DTO representing log record of established friendship
 */
class Connection(
    val p: Int,
    val q: Int,
    val timestamp: Long
)

/**
 * Custom UF extension with added functionality
 */
class FriendBookUnionFind(size: Int) : UnionFind(size) {

    /**
     * Remaining separate groups of people (trees in uf)
     */
    var groups = size

    override fun union(p: Int, q: Int): Boolean {
        val success = super.union(p, q)
        if (success) groups--
        return success
    }

    fun allConnected(): Boolean = groups == 1

    override fun toString(): String = "$groups ${super.toString()}"
}


/**
 * Question 2
 *
 * Hint: maintain an extra array to the weighted quick-union data structure
 * that stores for each root i the large element in the connected component containing i.
 */
class ModifiedUnionFind(size: Int) : UnionFind(size) {
    /**
     * find largest number in component containing [i]
     * time: O(N*logN)
     * optimal solution might use binary trees
     */
    fun findLargest(i: Int): Int {
        //find all elements of that component
        val root = root(i)
        val elements = mutableListOf<Int>()
        for (j in ids.indices) {
            if (root(j) == root) elements.add(j)
        }

        return elements.maxOrNull() ?: i
    }

}

/**
 * Question 3
 *
 * Successor with delete.
 * Given a set of n integers S={0,1,...,n−1} and a sequence of requests of the following form:
 * Remove x from S
 * Find the successor of x: the smallest y in S such that y≥x.
 *
 * design a data type so that all operations (except construction) take logarithmic time or better in the worst case.
 * Hint: use the modification of the union−find data discussed in the previous question.
 */
fun main() {
    val successor = SuccessorWithDelete(10)
    println(successor)
}

/**
 * data structure represented by an array
 * indices are initial set of integers
 * values are references to successor
 */
class SuccessorWithDelete(private var size: Int) {

    companion object {
        const val DELETED = -2//mark deleted integers
        const val NULL = -1//item has no successor (the biggest in the set)
    }

    private val items = IntArray(size)

    init {
        for (i in items.indices) {
            if (i < items.size - 1) {
                items[i] = i + 1//pointer to successor
            } else {
                items[i] = NULL
            }
        }
    }

    override fun toString(): String =
        "SuccessorWithDelete of $size,\nids: ${items.contentToString()}"

    fun remove(x: Int): Boolean {
        if (x >= size) throw IllegalArgumentException("x: $x is out of bounds")
        if (items[x] == DELETED) return false
        val xSuccessor = items[x]
        items[x] = DELETED

        //for logN complexity this could be using binary search
        for (i in x downTo 0) {
            if (items[i] != DELETED) {
                items[i] = xSuccessor
                break
            }
        }
        return true
    }

    fun successor(x: Int): Int {
        //validate x
        if (x >= size) throw IllegalArgumentException("x: $x is out of bounds")
        if (items[x] == DELETED) throw IllegalArgumentException("x: $x was deleted")
        if (items[x] == NULL) return x
        //return item x is pointing to
        return items[x]
    }
}
