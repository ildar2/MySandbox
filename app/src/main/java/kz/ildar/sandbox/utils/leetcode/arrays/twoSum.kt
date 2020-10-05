package kz.ildar.sandbox.utils.leetcode.arrays

/**
 * Given an array of integers nums and an integer target,
 * return indices of the two numbers such that they add up to target.
 *
 *  You may assume that each input would have exactly one
 *  solution, and you may not use the same element twice.
 *
 *  You can return the answer in any order.
 */
fun twoSum(nums: IntArray, target: Int): IntArray = twoSumHashMap1pass(nums, target)

/**
 * invalid, because after sort indices are mixed
 */
private fun twoSumSort(nums: IntArray, target: Int): IntArray {
    nums.sort()
    var i = 0
    var j = nums.lastIndex
    while(i < j) {
        println("comparing ${nums[i]} and ${nums[j]} to $target")
        when {
            nums[i] + nums[j] == target -> return intArrayOf(i, j)
            nums[i] + nums[j] < target -> i++
            else -> j--
        }
    }
    throw IllegalArgumentException("No two sum solution")
}

private fun twoSumNaive(nums: IntArray, target: Int): IntArray {
    for (i in nums.indices) {
        for (j in i + 1..nums.lastIndex) {
            if (nums[i] + nums[j] == target) return intArrayOf(i, j)
        }
    }
    throw IllegalArgumentException("No two sum solution")
}

private fun twoSumHashMap2pass(nums: IntArray, target: Int): IntArray {
    val memory = hashMapOf<Int, Int>()
    for (i in nums.indices) {
        memory[nums[i]] = i
    }
    for (i in nums.indices) {
        memory[target - nums[i]]?.let { j ->
            if (i != j) return intArrayOf(i, j)
        }
    }

    throw IllegalArgumentException("No two sum solution")
}

private fun twoSumHashMap1pass(nums: IntArray, target: Int): IntArray {
    val memory = hashMapOf<Int, Int>()

    nums.forEachIndexed { i, item ->
        memory[target - item]?.let { j ->
            if (i != j) return intArrayOf(j, i)
        }
        memory[item] = i
    }

    throw IllegalArgumentException("No two sum solution")
}
