package kz.ildar.sandbox.utils.leetcode.arrays

/**
 * rotate given matrix 90 degrees clockwise
 * no copy
 *
 * [[1, 2, 3],
 *  [4, 5, 6],
 *  [7, 8, 9]]
 */
fun rotateSquareMatrix(matrix: Array<IntArray>) {
    if (matrix.isEmpty()) return
    val n = matrix.size

    for (i in 0 until n) {
        for (j in 0 until n - i) {
            val temp = matrix[i][j]
            matrix[i][j] = matrix[n - j - 1][n - i - 1]
            matrix[n - j - 1][n - i - 1] = temp
        }
    }

    for (j in 0 until n) {
        for (i in 0 until n / 2) {
            val temp = matrix[i][j]
            matrix[i][j] = matrix[n - i - 1][j]
            matrix[n - i - 1][j] = temp
        }
    }
}

private fun print(matrix: Array<IntArray>) {
    if (matrix.isEmpty()) return
    val sb = StringBuilder()
    for (i in matrix.indices) {
        for (j in matrix[0].indices) {
            sb.append(matrix[i][j]).append("\t")
        }
        sb.append("\n")
    }
    println(sb)
}

fun sumOfDiagonals(matrix: Array<IntArray>) {
    val n = matrix.size
    var sum = 0
    for (i in 0 until n) {
        sum += matrix[i][i]
    }
    var j = n - 1

    for (i in 0 until n) {
        if (i != j) {
            sum += matrix[i][j]
        }
        j--
    }

    println("sum of diagonals is $sum")
}


/**
 * given non-square matrix with n rows and k columns
 * rotate it clockwise into new matrix
 */
fun rotateMatrix(m: Array<IntArray>): Array<IntArray> {
    if (m.isEmpty()) return arrayOf()
    val n = m.size
    val k = m[0].size

    val newM = Array(k) {
        IntArray(n)
    }

    for (i in 0 until n) {
        for (j in 0 until k) {
            newM[j][n - i - 1] = m[i][j]
        }
    }

    return newM
}

fun main() {
    val m = arrayOf(intArrayOf(1, 2, 3, 4), intArrayOf(5, 6, 7, 8), intArrayOf(9, 10, 11, 12), intArrayOf(13, 14, 15, 16))
    print(m)
    val newM = rotateMatrix(m)
    print(newM)

    val m1 = arrayOf(intArrayOf(1, 2, 3), intArrayOf(4, 5, 6), intArrayOf(7, 8, 9), intArrayOf(10, 11, 12))
    print(m1)
    val newM1 = rotateMatrix(m1)
    print(newM1)
}