package kz.ildar.sandbox.utils.leetcode.intervals

/**
 * https://leetcode.com/problems/non-overlapping-intervals/
 */
class NonOverlappingIntervalsSolution {
    /**
     * Given an array of [intervals] where intervals[i] = [starti, endi],
     * return the minimum number of intervals you need to remove to make the rest of the intervals non-overlapping.
     */
    fun eraseOverlapIntervals(intervals: Array<IntArray>): Int {
        if (intervals.isEmpty()) return 0
        intervals.sortWith(kotlin.Comparator { o1, o2 -> o1[1] - o2[1] })
        var count = 0
        var cur = intervals[0][1]
        for (i in 1..intervals.lastIndex) {
            if (intervals[i][0] >= cur) {
                cur = intervals[i][1]
            } else {
                count++
                cur = minOf(intervals[i][1], cur)
            }
        }
        return count
    }

    private fun eraseMy(intervals: Array<IntArray>): Int {
        if (intervals.isEmpty()) return 0
        intervals.sortWith(Comparator { a, b ->
            a[1] - b[1]
        })
        var current = intervals.first()
        var maxCap = 1

        for (i in 1 until intervals.size) {
            if (intervals[i][0] < current[1]) continue
            current = intervals[i]
            maxCap++
        }

        return intervals.size - maxCap
    }
}