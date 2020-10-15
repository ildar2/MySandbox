package kz.ildar.sandbox.utils.mock

/**
 *
diagonal traverse https://leetcode.com/problems/diagonal-traverse-ii/
array of arrays (not matrix - arrays can be of different sizes)
at most 10^5 elements
numbers are 1 to 10^9

input1:
1 2 3
4 5 6
7 8 9
output: 1 4 2 7 5 3 8 6 9

input2:
1 2 3 4 5
6 7
8
9 10 11
12 13 14 15 16
output: 1 6 2 8 7 3 9 4 12 10 5 13 11 14 15 16
 */
fun diagTraverse(input: Array<IntArray>): ArrayList<Int> {
    val result = ArrayList<Int>()
    val n = input.size

    //for square matrix
    var sum = 0
    var rising = true
    while(rising || sum >= 0) {//for each diagonal
        print("sum is $sum, rising: $rising.")
        var i = sum
        var j = 0
        while(i >= 0 && j < n) {
            print("[$i,$j] ")
            result.add(input[i][j])
            i--
            j++
        }

        println()
        if (sum == n - 1) rising = false
        if (rising) sum++
        else sum--
    }
    return result
}