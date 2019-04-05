package kz.ildar.sandbox.acm.bridges

import junit.framework.Assert.assertEquals
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.junit.Ignore
import org.junit.Test

class BridgesCalculatorTest {
    @Test(expected = IllegalArgumentException::class)
    fun testValidation() {
        BridgesCalculator(-5)
    }

    @Test
    fun testBuildPillars() {
        val calculator = BridgesCalculator()
        assertEquals(60, calculator.buildPillar(1))
        assertEquals(40, calculator.buildPillar(2))
        assertEquals(50, calculator.buildPillar(3))
        assertEquals(30, calculator.buildPillar(4))
        assertEquals(40, calculator.buildPillar(5))
    }

    @Test
    fun testChoosePoints() {
        val calculator = BridgesCalculator()
        assertArrayEquals(arrayOf(70), calculator.choosePoints())
        assertArrayEquals(arrayOf(20, 50), calculator.choosePoints(2))
        assertArrayEquals(arrayOf(30, 40), calculator.choosePoints(3))
        assertArrayEquals(arrayOf(50, 20), calculator.choosePoints(4))
        assertArrayEquals(arrayOf(20, 10, 40), calculator.choosePoints(2, 3))
        assertArrayEquals(arrayOf(20, 30, 20), calculator.choosePoints(2, 4))
        assertArrayEquals(arrayOf(30, 20, 20), calculator.choosePoints(3, 4))
        assertArrayEquals(arrayOf(20, 10, 20, 20), calculator.choosePoints(2, 3, 4))
    }

    @Test
    fun testGetPillars() {
        val calculator = BridgesCalculator()
        assertThat(calculator.getPillars(), `is`(listOf(60, 40)))
        assertThat(calculator.getPillars(2), `is`(listOf(60, 40, 40)))
        assertThat(calculator.getPillars(3), `is`(listOf(60, 50, 40)))
        assertThat(calculator.getPillars(4), `is`(listOf(60, 30, 40)))
        assertThat(calculator.getPillars(2, 3), `is`(listOf(60, 40, 50, 40)))
        assertThat(calculator.getPillars(2, 4), `is`(listOf(60, 40, 30, 40)))
        assertThat(calculator.getPillars(3, 4), `is`(listOf(60, 50, 30, 40)))
        assertThat(calculator.getPillars(2, 3, 4), `is`(listOf(60, 40, 50, 30, 40)))
    }

    @Test
    fun testCalculation() {
        val calculator = BridgesCalculator()
        assertEquals(6460, calculator.calcBridge(2, 4))
    }

    @Test
    fun testPossiblePillarPlacements() {
        val calculator = BridgesCalculator()
        assertThat(calculator.possiblePlacements(), `is`(setOf(
            emptyArray(),
            arrayOf(2),
            arrayOf(3),
            arrayOf(4),
            arrayOf(2, 3),
            arrayOf(2, 4),
            arrayOf(3, 4),
            arrayOf(2, 3, 4)
        )))
    }

    @Test
    fun testPermute() {
        val calculator = BridgesCalculator()
        assertThat(calculator.permute(), `is`(setOf()))
        assertThat(calculator.permute(1), `is`(setOf(
            emptyArray(),
            arrayOf(1)
        )))
    }

    @Test
    @Ignore
    fun testBridgeNotPossible() {
        fail()
    }
}