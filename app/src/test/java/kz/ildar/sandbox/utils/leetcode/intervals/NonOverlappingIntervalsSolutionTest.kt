package kz.ildar.sandbox.utils.leetcode.intervals

import org.junit.Assert.assertEquals
import org.junit.Test

class NonOverlappingIntervalsSolutionTest {

    /**
     * Input: intervals = [[1,2],[2,3],[3,4],[1,3]]
     * Output: 1
     * Explanation: [1,3] can be removed and the rest of the intervals are non-overlapping.
     */
    @Test
    fun example1() {
        assertEquals(
            1, NonOverlappingIntervalsSolution().eraseOverlapIntervals(
                arrayOf(
                    intArrayOf(1, 2),
                    intArrayOf(2, 3),
                    intArrayOf(3, 4),
                    intArrayOf(1, 3),
                )
            )
        )
    }

    /**
     * Input: intervals = [[1,2],[1,2],[1,2]]
     * Output: 2
     * Explanation: You need to remove two [1,2] to make the rest of the intervals non-overlapping.
     */
    @Test
    fun example2() {
        assertEquals(
            2, NonOverlappingIntervalsSolution().eraseOverlapIntervals(
                arrayOf(
                    intArrayOf(1, 2),
                    intArrayOf(1, 2),
                    intArrayOf(1, 2),
                )
            )
        )
    }

    /**
     * Input: intervals = [[1,2],[2,3]]
     * Output: 0
     * Explanation: You don't need to remove any of the intervals since they're already non-overlapping.
     */
    @Test
    fun example3() {
        assertEquals(
            0, NonOverlappingIntervalsSolution().eraseOverlapIntervals(
                arrayOf(
                    intArrayOf(1, 2),
                    intArrayOf(2, 3),
                )
            )
        )
    }

    /**
     * Input: intervals = [[1,100],[11,22],[1,11],[2,12]]
     * Output: 2
     * Explanation: first should be removed for the best result (and the last one)
     */
    @Test
    fun example4() {
        assertEquals(
            2, NonOverlappingIntervalsSolution().eraseOverlapIntervals(
                arrayOf(
                    intArrayOf(1, 100),
                    intArrayOf(11, 22),
                    intArrayOf(1, 11),
                    intArrayOf(2, 12),
                )
            )
        )
    }
}