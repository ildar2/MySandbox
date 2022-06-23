package kz.ildar.sandbox.utils.leetcode.intervals

/**
 * https://aaronice.gitbook.io/lintcode/sweep-line/meeting-rooms
 */
class MeetingRoomsSolution {
    /**
     * [[0,30],[5,10],[15,20]]
     */
    fun canAttendMeetings(times: Array<IntArray>): Boolean {
        if (times.isEmpty()) return true
        times.sortBy {
            it[0]
        }
        var last = times.first()
        for (i in 1..times.lastIndex) {
            if (times[i][0] < last[1]) return false
            last = times[i]
        }
        return true
    }
}