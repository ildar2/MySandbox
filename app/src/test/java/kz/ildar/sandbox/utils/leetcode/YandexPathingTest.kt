package kz.ildar.sandbox.utils.leetcode

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class YandexPathingTest {

    private val mazeMap: Array<out IntArray> = arrayOf(
        intArrayOf(0, 0, 1, 0),
        intArrayOf(1, 0, 0, 0),
        intArrayOf(0, 0, 1, 1),
        intArrayOf(1, 0, 0, 0)
    )

    @Test
    fun testDefault() {
        println("testDefault")
        assertTrue(hasSolution(mazeMap, 0, 0, 3, 3))
    }

    @Test
    fun testFromMiddle() {
        println("testFromMiddle")
        assertTrue(hasSolution(mazeMap, 2, 1, 3, 3))
    }

    @Test
    fun testFromTheWall() {
        println("testFromTheWall")
        assertFalse(hasSolution(mazeMap, 0, 1, 3, 3))
    }

    @Test
    fun testFromTheEdge() {
        println("testFromTheEdge")
        assertTrue(hasSolution(mazeMap, 3, 0, 3, 3))
    }

    @Test
    fun testToTheWall() {
        println("testToTheWall")
        assertFalse(hasSolution(mazeMap, 0, 0, 2, 0))
    }

    private val mazeMapBlocked: Array<out IntArray> = arrayOf(
        intArrayOf(0, 1, 1, 0),
        intArrayOf(1, 0, 0, 0),
        intArrayOf(0, 0, 1, 1),
        intArrayOf(1, 0, 0, 0)
    )

    @Test
    fun testBlocked() {
        println("testBlocked")
        assertFalse(hasSolution(mazeMapBlocked, 0, 0, 3, 3))
    }

    private val mazeMapOpen: Array<out IntArray> = arrayOf(
        intArrayOf(0, 0, 0, 0, 0, 0),
        intArrayOf(0, 0, 0, 0, 0, 0),
        intArrayOf(0, 0, 0, 0, 0, 0),
        intArrayOf(0, 0, 0, 0, 0, 0),
        intArrayOf(0, 0, 0, 0, 0, 0)
    )

    @Test
    fun testOpenMap() {
        println("testOpenMap")
        assertTrue(hasSolution(mazeMapOpen, 0, 0, 4, 1))
    }
}