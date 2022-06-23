package kz.ildar.sandbox.utils.leetcode.intervals

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MeetingRoomsSolutionTest {

    /**
     * input: [[0,30],[5,10],[15,20]]
     * output: false
     * explanation: first time conflicts with other two
     */
    @Test
    fun example1() {
        assertFalse(
            MeetingRoomsSolution().canAttendMeetings(
                arrayOf(
                    intArrayOf(0, 30),
                    intArrayOf(5, 10),
                    intArrayOf(15, 20),
                )
            )
        )
    }

    /**
     * input: [[7,10],[2,4]]
     * output: true
     * explanation: no clashes
     */
    @Test
    fun example2() {
        assertTrue(
            MeetingRoomsSolution().canAttendMeetings(
                arrayOf(
                    intArrayOf(7, 10),
                    intArrayOf(2, 4),
                )
            )
        )
    }
}