package kz.ildar.sandbox.utils.coursera.union_find

import org.junit.Assert.*
import org.junit.Test

class ExamKtTest {


    @Test
    fun testQuestion1() {
        assertEquals(-1, question1(4, listOf()))
        assertEquals(4, question1(5, listOf(
            Connection(0, 1, 1),
            Connection(1, 2, 2),
            Connection(2, 3, 3),
            Connection(3, 4, 4),
            Connection(4, 1, 5)
        )))
    }
}