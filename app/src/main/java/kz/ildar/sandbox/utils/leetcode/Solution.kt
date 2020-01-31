package kz.ildar.sandbox.utils.leetcode

class Solution {
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
        val value: Int,
        val char: String
    ) {
        M(1000, "M"),
        D(500, "D"),
        C(100, "C"),
        L(50, "L"),
        X(10, "X"),
        V(5, "V"),
        I(1, "I")
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
            for (char in Roman.values()) {
                if (temp >= char.value) {
                    output.append(char.char)
                    temp -= char.value
                    //run 4,9,40,90 substitution
                    for (entry in substitutionList) {
                        val corrected = output.replace(entry.first.toRegex(), entry.second)
                        output.delete(0, output.length)
                        output.append(corrected)
                    }
                    break
                }
            }
        }
        return output.toString()
    }
}