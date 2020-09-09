package kz.ildar.sandbox.utils.leetcode.unionFind

/**
 * Weighted Union-find
 *
 * init: O(N)
 * union: O(logN)
 * find: O(logN)
 */
class UnionFind(
    private val size: Int
) {
    private val ids = IntArray(size)
    private val sizes = IntArray(size)

    /**
     * each node points to itself
     */
    init {
        for (i in ids.indices) {
            ids[i] = i
            sizes[i] = 1
        }
    }

    override fun toString(): String =
        "UnionFind of $size,\nids: ${ids.contentToString()}\nsizes: ${sizes.contentToString()}"

    fun union(p: Int, q: Int) {
        val pRoot = root(p)
        val qRoot = root(q)
        if (pRoot == qRoot) return

        val pSize = sizes[pRoot]
        val qSize = sizes[qRoot]

        if (pSize < qSize) {
            ids[pRoot] = qRoot
            sizes[q] += pSize
        } else {
            ids[qRoot] = pRoot
            sizes[p] += qSize
        }
    }

    fun find(p: Int, q: Int): Boolean = root(p) == root(q)

    /**
     *   5
     * 6   7
     * 2
     */
    private fun root(node: Int): Int {
        var root = ids[node]
        while(root != ids[root]) {
            //room for improvement - set root to grandparent
            root = ids[root]
        }
        return root
    }
}

fun main() {
    val uf = UnionFind(10)
    println(uf)
    uf.union(4, 5)
    println(uf)
    println(uf.find(1, 4))
    uf.union(1, 2)
    uf.union(1, 5)
    println(uf)
    println(uf.find(1, 4))
}
