package kz.ildar.sandbox.utils.leetcode.interview

import org.junit.Assert.assertArrayEquals
import org.junit.Test
import java.io.File
import java.util.*
import kotlin.random.Random

class InterviewKtTest {

    @Test
    fun testEmpty() {
        assertArrayEquals(intArrayOf(), sumExceptI(intArrayOf()))
    }

    @Test
    fun testOne() {
        assertArrayEquals(intArrayOf(0), sumExceptI(intArrayOf(3)))
    }

    @Test
    fun testBasic() {
        assertArrayEquals(intArrayOf(4, 3, 5), sumExceptI(intArrayOf(2, 3, 1)))
    }

    @Test
    fun testBigNumbers() {
        assertArrayEquals(
            intArrayOf(120446435, 67091213, 74911924, 99193435),
            sumExceptI(intArrayOf(101234, 53456456, 45635745, 21354234))
        )
    }

    @Test
    fun testSize160() {
        assertArrayEquals(
            intArrayOf(1331519, 1330911, 1330998, 1331529, 1330330, 1331432, 1331512, 1331560, 1331139, 1329219, 1259219, 1308112, 1331541, 1327687, 1239241, 1331531, 1331519, 1331559, 1331508, 1331559, 1331558, 1331497, 1331558, 1331488, 1331556, 1331557, 1331556, 1331486, 1331557, 1331555, 1331475, 1331475, 1331556, 1274775, 986326, 1331555, 1331475, 1331557, 1331477, 1331497, 1331558, 1331557, 1331558, 1331557, 1331558, 1331557, 1324797, 1331558, 1331557, 1330888, 1331557, 1324800, 1331508, 1331212, 1331562, 1331519, 1331562, 1331541, 1331519, 1331329, 1331562, 1331040, 1329219, 1329219, 1331519, 1331499, 1294108, 1331208, 1326786, 1331208, 1331318, 1331190, 1331501, 1331540, 1331038, 1331527, 1331516, 1331518, 1331199, 1331208, 1331519, 1330911, 1330998, 1331529, 1330330, 1331432, 1331512, 1331560, 1331139, 1329219, 1259219, 1308112, 1331541, 1327687, 1239241, 1331531, 1331519, 1331559, 1331508, 1331559, 1331558, 1331497, 1331558, 1331488, 1331556, 1331557, 1331556, 1331486, 1331557, 1331555, 1331475, 1331475, 1331556, 1274775, 986326, 1331555, 1331475, 1331557, 1331477, 1331497, 1331558, 1331557, 1331558, 1331557, 1331558, 1331557, 1324797, 1331558, 1331557, 1330888, 1331557, 1324800, 1331508, 1331212, 1331562, 1331519, 1331562, 1331541, 1331519, 1331329, 1331562, 1331040, 1329219, 1329219, 1331519, 1331499, 1294108, 1331208, 1326786, 1331208, 1331318, 1331190, 1331501, 1331540, 1331038, 1331527, 1331516, 1331518, 1331199, 1331208),
            sumExceptI(intArrayOf(45, 653, 566, 35, 1234, 132, 52, 4, 425, 2345, 72345, 23452, 23, 3877, 92323, 33, 45, 5, 56, 5, 6, 67, 6, 76, 8, 7, 8, 78, 7, 9, 89, 89, 8, 56789, 345238, 9, 89, 7, 87, 67, 6, 7, 6, 7, 6, 7, 6767, 6, 7, 676, 7, 6764, 56, 352, 2, 45, 2, 23, 45, 235, 2, 524, 2345, 2345, 45, 65, 37456, 356, 4778, 356, 246, 374, 63, 24, 526, 37, 48, 46, 365, 356, 45, 653, 566, 35, 1234, 132, 52, 4, 425, 2345, 72345, 23452, 23, 3877, 92323, 33, 45, 5, 56, 5, 6, 67, 6, 76, 8, 7, 8, 78, 7, 9, 89, 89, 8, 56789, 345238, 9, 89, 7, 87, 67, 6, 7, 6, 7, 6, 7, 6767, 6, 7, 676, 7, 6764, 56, 352, 2, 45, 2, 23, 45, 235, 2, 524, 2345, 2345, 45, 65, 37456, 356, 4778, 356, 246, 374, 63, 24, 526, 37, 48, 46, 365, 356))
        )
    }

    @Test
    fun testSize16000() {
        assertArrayEquals(
            readArray("C:\\Android\\keys\\16000_result.txt", 16000),
            sumExceptI(readArray("C:\\Android\\keys\\16000_input.txt", 16000))
        )
    }

    private fun readArray(url: String, size: Int): IntArray {
        val array = IntArray(size)
        try {
            val scanner = Scanner(File(url))
                .useDelimiter(", ")
            var i = 0

            while(scanner.hasNextInt()) {
                array[i++] = scanner.nextInt()
            }
        } catch (e: Exception) {
            System.err.println(e.stackTrace.contentToString())
        }
        return array
    }

    private fun genArray(size: Int, max: Int): IntArray {
        val array = IntArray(size)
        val random = Random(System.currentTimeMillis())
        for (i in array.indices) {
            array[i] = random.nextInt(0, max)
        }
        return array
    }
}