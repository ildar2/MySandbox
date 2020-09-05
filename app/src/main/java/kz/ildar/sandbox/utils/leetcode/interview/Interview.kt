package kz.ildar.sandbox.utils.leetcode.interview

/**
 * Дан массив [array] длиной N
 * Нужно вычислить массив, где каждое i-e значение -
 * это сумма всех значений массива [array], кроме значения на i
 * array:  [2, 3, 1]
 * output: [4, 3, 5]
 */
fun sumExceptI(
    array: IntArray
): IntArray {
    return InterviewJava.sumOptimal(array)
//    return sumOptimal(array)
}

private fun sumOptimal(array: IntArray): IntArray {
    val result = IntArray(array.size)

    val sum = array.sum()

    for (index in array.indices) {
        result[index] = sum - array[index]
    }
    return result
}

private fun sumNotOptimal(array: IntArray): IntArray {
    val result = IntArray(array.size)

    for (i in array.indices) {
        var sum = 0
        for (j in array.indices) {
            if (i != j) {
                sum += array[j]
            }
        }
        result[i] = sum
    }

    return result
}