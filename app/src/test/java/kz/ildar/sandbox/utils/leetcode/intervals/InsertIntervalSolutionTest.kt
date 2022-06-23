package kz.ildar.sandbox.utils.leetcode.intervals

import org.junit.Assert.assertArrayEquals
import org.junit.Test

internal class InsertIntervalSolutionTest {

    /**
     * Input: intervals = [[1,3],[6,9]], newInterval = [2,5]
     * Output: [[1,5],[6,9]]
     */
    @Test
    fun example1() {
        assertArrayEquals(
            arrayOf(
                intArrayOf(1, 5),
                intArrayOf(6, 9),
            ),
            InsertIntervalSolution().insert(
                arrayOf(
                    intArrayOf(1, 3),
                    intArrayOf(6, 9),
                ),
                intArrayOf(2, 5)
            ),
        )
    }

    /**
     * Input: intervals = [[1,2],[3,5],[6,7],[8,10],[12,16]], newInterval = [4,8]
     * Output: [[1,2],[3,10],[12,16]]
     * Explanation: Because the new interval [4,8] overlaps with [3,5],[6,7],[8,10].
     */
    @Test
    fun example2() {
        assertArrayEquals(
            arrayOf(
                intArrayOf(1, 2),
                intArrayOf(3, 10),
                intArrayOf(12, 16),
            ),
            InsertIntervalSolution().insert(
                arrayOf(
                    intArrayOf(1, 2),
                    intArrayOf(3, 5),
                    intArrayOf(6, 7),
                    intArrayOf(8, 10),
                    intArrayOf(12, 16),
                ),
                intArrayOf(4, 8)
            ),
        )
    }

    @Test
    fun testSimple() {
        assertArrayEquals(
            arrayOf(
                intArrayOf(1, 2),
                intArrayOf(4, 5),
                intArrayOf(9, 10),
                intArrayOf(12, 16),
            ),
            InsertIntervalSolution().insert(
                arrayOf(
                    intArrayOf(1, 2),
                    intArrayOf(9, 10),
                    intArrayOf(12, 16),
                ),
                intArrayOf(4, 5)
            ),
        )
    }

    @Test
    fun testEdgeLeft() {
        assertArrayEquals(
            arrayOf(
                intArrayOf(1, 5),
            ),
            InsertIntervalSolution().insert(
                arrayOf(
                    intArrayOf(3, 5),
                ),
                intArrayOf(1, 4)
            ),
        )
    }

    @Test
    fun testEdgeLeft2() {
        assertArrayEquals(
            arrayOf(
                intArrayOf(0, 5),
            ),
            InsertIntervalSolution().insert(
                arrayOf(
                    intArrayOf(1, 5),
                ),
                intArrayOf(0, 1)
            ),
        )
    }

    @Test
    fun testEdgeRight() {
        assertArrayEquals(
            arrayOf(
                intArrayOf(1, 5),
            ),
            InsertIntervalSolution().insert(
                arrayOf(
                    intArrayOf(1, 4),
                ),
                intArrayOf(3, 5)
            ),
        )
    }

    @Test
    fun testEdgeRight2() {
        assertArrayEquals(
            arrayOf(
                intArrayOf(1, 5),
            ),
            InsertIntervalSolution().insert(
                arrayOf(
                    intArrayOf(1, 4),
                ),
                intArrayOf(4, 5)
            ),
        )
    }

    @Test
    fun testEdgeWrap() {
        assertArrayEquals(
            arrayOf(
                intArrayOf(0, 6),
            ),
            InsertIntervalSolution().insert(
                arrayOf(
                    intArrayOf(1, 5),
                ),
                intArrayOf(0, 6)
            ),
        )
    }

    @Test
    fun testEdgeWrap2() {
        assertArrayEquals(
            arrayOf(
                intArrayOf(0, 5),
                intArrayOf(7, 16),
            ),
            InsertIntervalSolution().insert(
                arrayOf(
                    intArrayOf(0, 5),
                    intArrayOf(9, 12),
                ),
                intArrayOf(7, 16)
            ),
        )
    }

    @Test
    fun testEdgeWrap3() {
        assertArrayEquals(
            arrayOf(
                intArrayOf(0, 6),
                intArrayOf(9, 12),
            ),
            InsertIntervalSolution().insert(
                arrayOf(
                    intArrayOf(1, 5),
                    intArrayOf(9, 12),
                ),
                intArrayOf(0, 6)
            ),
        )
    }

    @Test
    fun testEdgeInside() {
        assertArrayEquals(
            arrayOf(
                intArrayOf(0, 6),
            ),
            InsertIntervalSolution().insert(
                arrayOf(
                    intArrayOf(0, 6),
                ),
                intArrayOf(1, 5)
            ),
        )
    }

    @Test
    fun testEmpty() {
        assertArrayEquals(
            arrayOf(
                intArrayOf(4, 5),
            ),
            InsertIntervalSolution().insert(
                arrayOf(),
                intArrayOf(4, 5)
            ),
        )
    }
}