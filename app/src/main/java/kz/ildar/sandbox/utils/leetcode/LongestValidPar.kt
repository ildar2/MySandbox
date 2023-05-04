package kz.ildar.sandbox.utils.leetcode

class LongestValidPar {
    fun longestValidParentheses(s: String): Int {
        val dp = IntArray(s.length)
        var maxLength = 0
        for (i in 1 until s.length) {
            if (s[i] == ')') {
                if (s[i - 1] == '(') {
                    dp[i] = if (i >= 2) dp[i - 2] + 2 else 2
                } else if (i - dp[i - 1] > 0 && s[i - dp[i - 1] - 1] == '(') {
                    dp[i] = dp[i - 1] + (if ((i - dp[i - 1]) >= 2) dp[i - dp[i - 1] - 2] else 0) + 2
                }
                maxLength = maxOf(maxLength, dp[i])
            }
        }
        return maxLength
    }
}