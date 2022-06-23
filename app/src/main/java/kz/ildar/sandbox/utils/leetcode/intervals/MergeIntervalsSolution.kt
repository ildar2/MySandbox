package kz.ildar.sandbox.utils.leetcode.intervals

/**
 * https://leetcode.com/problems/merge-intervals/
 */
class MergeIntervalsSolution {
    /**
     * Input: intervals = [[1,3],[2,6],[8,10],[15,18]]
     * Output: [[1,6],[8,10],[15,18]]
     * Explanation: Since intervals [1,3] and [2,6] overlap, merge them into [1,6].
     */
    fun merge(intervals: Array<IntArray>): Array<IntArray> {
        return fastSolution(intervals)
    }

    private fun fastSolution(intervals: Array<IntArray>): Array<IntArray> {
        if (intervals.isEmpty()) return intervals

        intervals.sortBy { it[0] }

        val res = arrayListOf(intervals.first())

        for (i in 1 until intervals.size) {
            val last = res.last()
            if (intervals[i][0] <= last[1]) {
                last[1] = kotlin.math.max(intervals[i][1], last[1])
            } else {
                res.add(intervals[i])
            }
        }

        return res.toTypedArray()
    }

    private fun ifUnsorted(intervals: Array<IntArray>): Array<IntArray> {
        intervals.sortBy { it[0] }
        return ifSorted(intervals)
    }

    private fun ifSorted(intervals: Array<IntArray>): Array<IntArray> {
        val res = mutableListOf<IntArray>()

        if (intervals.isEmpty()) return intervals

        var start = intervals[0][0]
        var end = intervals[0][1]
        var counter = 0

        while (counter < intervals.size) {
            //check for running merge
            if (end >= intervals[counter][0]) {
                end = Math.max(end, intervals[counter][1])
            }

            //end merge and continue
            if (end < intervals[counter][0]) {
                res.add(intArrayOf(start, end))
                start = intervals[counter][0]
                end = intervals[counter][1]
            }
            counter++
        }
        res.add(intArrayOf(start, end))

        return res.toTypedArray()
    }

    private fun Collection<IntArray>.print() {
        print("[")
        forEach {
            print(it.contentToString())
            print(",")
        }
        println("]")
    }
}