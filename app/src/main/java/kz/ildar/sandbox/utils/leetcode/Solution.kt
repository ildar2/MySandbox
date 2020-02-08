package kz.ildar.sandbox.utils.leetcode

class Solution {

    companion object {

        fun overflowIssues() {
            val number: Int = 400000000
            val squared = number * number
            println(squared)
            val squaredLong: Long = number.toLong() * number
            println(squaredLong)
        }
    }

    fun productExceptSelf(nums: IntArray): IntArray {
        var temp = 1
        val prod = IntArray(nums.size) { 1 }

        for (index in nums.indices) {
            prod[index] = temp
            temp *= nums[index]
        }
        temp = 1
        for (index in nums.indices.reversed()) {
            prod[index] *= temp
            temp *= nums[index]
        }
        return prod
    }

    enum class Roman(
        val value: Int
    ) {
        M(1000),
        D(500),
        C(100),
        L(50),
        X(10),
        V(5),
        I(1)
    }

    enum class RomanExtended(
        val value: Int
    ) {
        M(1000),
        CM(900),
        D(500),
        CD(400),
        C(100),
        XC(90),
        L(50),
        XL(40),
        X(10),
        IX(9),
        V(5),
        IV(4),
        I(1)
    }

    private val substitutionList = listOf(
        "DCCCC" to "CM",
        "CCCC" to "CD",
        "LXXXX" to "XC",
        "XXXX" to "XL",
        "VIIII" to "IX",
        "IIII" to "IV"
    )

    fun numToRoman(input: Int = 5): String {
        val output = StringBuffer()
        //we will subtract Roman from input
        var temp = input
        while(temp > 0) {
            //find largest roman number
            for (char in RomanExtended.values()) {
                if (temp >= char.value) {
                    output.append(char.name)
                    temp -= char.value
                    break
                }
            }
        }
        return output.toString()
    }
}