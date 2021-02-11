package kz.ildar.sandbox.utils.leetcode.strings

/**
 * Сгенерировать все возможные скобки длинной [n]
 */
fun parenthesis(n: Int) {
    callCount = 0
    generate("", 0, 0, n)
    println("Call count: $callCount")
}

private var callCount = 0

private fun generate(cur: String, open: Int, closed: Int, n: Int) {
    callCount++
    if (cur.length == 2 * n) {
        println(cur)
        return
    }
    if (open < n) generate("$cur(", open + 1, closed, n)
    if (closed < open) generate("$cur)", open, closed + 1, n)
}

/**
 * Too much call count
 * 4 for n = 1
 * 13 for n = 2
 * 43 for n = 3
 */
private fun weakGen(cur: String, open: Int, closed: Int, n: Int) {
    callCount++
    if (cur.length == 2 * n) {
        if (open == closed) {
            println(cur)
        }
        return
    }
    weakGen("$cur(", open + 1, closed, n)
    if (closed < open) weakGen("$cur)", open, closed + 1, n)
}