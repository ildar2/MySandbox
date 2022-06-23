package kz.ildar.sandbox.utils.leetcode.intervals

import org.junit.Assert
import org.junit.Test

class MergeIntervalsSolutionTest {

    /**
     * Input: intervals = [[1,3],[2,6],[8,10],[15,18]]
     * Output: [[1,6],[8,10],[15,18]]
     * Explanation: Since intervals [1,3] and [2,6] overlap, merge them into [1,6].
     */
    @Test
    fun example1() {
        Assert.assertArrayEquals(
            arrayOf(
                intArrayOf(1, 6),
                intArrayOf(8, 10),
                intArrayOf(15, 18),
            ),
            MergeIntervalsSolution().merge(
                arrayOf(
                    intArrayOf(1, 3),
                    intArrayOf(2, 6),
                    intArrayOf(8, 10),
                    intArrayOf(15, 18),
                )
            ),
        )
    }

    /**
     * Input: intervals = [[1,4],[4,5]]
     * Output: [[1,5]]
     * Explanation: Intervals [1,4] and [4,5] are considered overlapping.
     */
    @Test
    fun example2() {
        Assert.assertArrayEquals(
            arrayOf(
                intArrayOf(1, 5),
            ),
            MergeIntervalsSolution().merge(
                arrayOf(
                    intArrayOf(1, 4),
                    intArrayOf(4, 5),
                )
            ),
        )
    }

    @Test
    fun testEmpty() {
        Assert.assertArrayEquals(
            arrayOf(),
            MergeIntervalsSolution().merge(
                arrayOf()
            ),
        )
    }

    @Test
    fun test1() {
        Assert.assertArrayEquals(
            arrayOf(
                intArrayOf(1, 5),
            ),
            MergeIntervalsSolution().merge(
                arrayOf(
                    intArrayOf(1, 5),
                )
            ),
        )
    }

    @Test
    fun test2() {
        Assert.assertArrayEquals(
            arrayOf(
                intArrayOf(1, 5),
            ),
            MergeIntervalsSolution().merge(
                arrayOf(
                    intArrayOf(1, 5),
                    intArrayOf(1, 5),
                )
            ),
        )
    }

    @Test
    fun test3() {
        Assert.assertArrayEquals(
            arrayOf(
                intArrayOf(1, 5),
                intArrayOf(7, 15),
            ),
            MergeIntervalsSolution().merge(
                arrayOf(
                    intArrayOf(1, 5),
                    intArrayOf(7, 15),
                )
            ),
        )
    }

    @Test
    fun test4() {
        Assert.assertArrayEquals(
            arrayOf(
                intArrayOf(1, 5),
            ),
            MergeIntervalsSolution().merge(
                arrayOf(
                    intArrayOf(1, 5),
                    intArrayOf(2, 3),
                )
            ),
        )
    }


    @Test
    fun testUnsorted() {
        Assert.assertArrayEquals(
            arrayOf(
                intArrayOf(0, 4),
            ),
            MergeIntervalsSolution().merge(
                arrayOf(
                    intArrayOf(1, 4),
                    intArrayOf(0, 4),
                )
            ),
        )
    }

}