package kz.ildar.sandbox.utils.leetcode.strings

import org.junit.Assert.*
import org.junit.Test

class FormatKtTest {

    @Test
    fun test1() {
        assertEquals("50", 50.seperate())
        assertEquals("99 999 999", 99999999.seperate())
        assertEquals("0", 0.seperate())
        assertEquals("-1", (-1).seperate())
        assertEquals("2 000 000 000", 2000_000_000.seperate())
    }
}

fun Int.seperate(): String {
    return String.format("%,d", this)
}