package kz.ildar.sandbox.utils.leetcode.intervals

import kotlin.math.min

/**
 * https://leetcode.com/problems/insert-interval/
 */
class InsertIntervalSolution {

    /**
     * Input: intervals = [[1,2],[3,5],[6,7],[8,10],[12,16]], newInterval = [4,8]
     * Output: [[1,2],[3,10],[12,16]]
     * Explanation: Because the new interval [4,8] overlaps with [3,5],[6,7],[8,10].
     *
     * 4-8
     * (1,2)(3,4,5)(6,7)(8,9,10)11(12,13,14,15,16)
     *
     * [2,5]
     * (1,2,3)4,5(6,7,8,9)
     * Output: [[1,5],[6,9]]
     */
    fun insert(intervals: Array<IntArray>, newInterval: IntArray): Array<IntArray> {
        return insertIntervalProper(intervals, newInterval)
    }

    private fun insertIntervalProper(
        intervals: Array<IntArray>,
        newInterval: IntArray
    ): Array<IntArray> {
        val res = mutableListOf<IntArray>()

        var start = newInterval[0]
        var end = newInterval[1]
        var counter = 0

        while (counter < intervals.size && intervals[counter][1] < start) res.add(intervals[counter++])

        while (counter < intervals.size && intervals[counter][0] <= end) {
            start = kotlin.math.min(start, intervals[counter][0])
            end = kotlin.math.max(end, intervals[counter++][1])
        }
        res.add(intArrayOf(start, end))

        while (counter < intervals.size) res.add(intervals[counter++])

        return res.toTypedArray()
    }

    private fun insertIntervalNaive(
        intervals: Array<IntArray>,
        newInterval: IntArray
    ): Array<IntArray> {
        fun Int.inInterval(interval: IntArray): Boolean {
            return interval[0] <= this && interval[1] >= this
        }

        val result = ArrayList<IntArray>()

        var added = false
        var adding = false
        var start = 0

        if (intervals.isNotEmpty()
            && newInterval[0] < intervals[0][0]
            && newInterval[1] >= intervals[0][0]
        ) {
            adding = true
            start = newInterval[0]
        }

        for (interval in intervals) {
            if (adding && newInterval[1] < interval[0]) {
                result.add(intArrayOf(start, newInterval[1]))
                adding = false
                added = true
            }
            if (!adding) {
                if (!added && newInterval[0] < interval[0] && newInterval[1] < interval[0]) {
                    result.add(newInterval)
                    added = true
                }
                if (!added && newInterval[0] <= interval[1]) {
                    adding = true
                    start = min(interval[0], newInterval[0])
                } else {
                    result.add(interval)
                }
            }
            if (adding) {
                if (newInterval[1].inInterval(interval)) {
                    adding = false
                    result.add(intArrayOf(start, interval[1]))
                    added = true
                }
            }
        }

        if (adding) {
            result.add(intArrayOf(start, newInterval[1]))
            added = true
        }

        if (!added) result.add(newInterval)

        return result.toTypedArray()
    }
}