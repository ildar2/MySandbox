package kz.ildar.sandbox.utils.leetcode.union_find

import org.junit.Assert.*
import org.junit.Test
import kotlin.test.assertFailsWith

class SuccessorWithDeleteTest {

    @Test
    fun testSuccessorRaw() {
        val successorWithDelete = SuccessorWithDelete(5)
        assertEquals(2, successorWithDelete.successor(1))
        assertFailsWith<IllegalArgumentException>(
            "x: 5 is out of bounds"
        ) {
            successorWithDelete.successor(5)
        }
        assertEquals(4, successorWithDelete.successor(4))
    }

    @Test
    fun testSuccessorDelete() {
        val successorWithDelete = SuccessorWithDelete(7)
        assertEquals(5, successorWithDelete.successor(4))

        successorWithDelete.remove(4)
        assertFailsWith<IllegalArgumentException>(
            "x: 4 was deleted"
        ) {
            successorWithDelete.successor(4)
        }
        assertEquals(5, successorWithDelete.successor(3))
        assertEquals(3, successorWithDelete.successor(2))
        successorWithDelete.remove(3)
        assertEquals(5, successorWithDelete.successor(2))
    }
}