package kz.ildar.sandbox.utils.leetcode.interview

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