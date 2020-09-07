package kz.ildar.sandbox.utils.leetcode.arrays

import java.util.*

const val MOD = 1e9.toInt() + 7

fun numWays(s: String): Int {
    var cnt = 0
    for (i in s.toCharArray()) {
        cnt += if (i == '1') 1 else 0
    }
    if (cnt % 3 != 0) {
        return 0
    }
    var ans = 0
    val dp = HashMap<Int, Int>()
    cnt /= 3
    var bal = 0
    var i = 0
    while(i + 1 < s.length) {
        bal += if (s[i] == '1') 1 else 0
        if (bal == cnt * 2) {
            ans += dp[cnt] ?: 0
            ans %= MOD
        }
        dp[bal] = dp[bal] ?: 0 + 1
        i++
    }
    return ans
}