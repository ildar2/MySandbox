package kz.ildar.sandbox.utils.leetcode.strings

import org.junit.Assert.assertEquals
import org.junit.Test

class ArrangeSentenceTest {

    @Test
    fun mainTest() {
        assertEquals("And cats hats.", arrange("Cats and hats."))
        assertEquals("And cats hats.", arrange("Cats  and hats."))
    }
}