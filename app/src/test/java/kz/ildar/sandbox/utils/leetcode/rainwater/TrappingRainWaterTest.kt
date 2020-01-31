package kz.ildar.sandbox.utils.leetcode.rainwater

import org.junit.Assert.assertEquals
import org.junit.Test

class TrappingRainWaterTest {
    @Test
    fun `test no water`() {
        assertEquals(0, calcTrappedWater(intArrayOf(0, 1, 0)))
    }

    @Test
    fun `test small pool 1`() {
        assertEquals(1, calcTrappedWater(intArrayOf(0, 1, 0, 1)))
    }

    @Test
    fun `test small pool 2`() {
        assertEquals(2, calcTrappedWater(intArrayOf(0, 1, 0, 0, 1)))
    }

    @Test
    fun `test leetcode input`() {
        assertEquals(6, calcTrappedWater(intArrayOf(0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1)))
    }

}