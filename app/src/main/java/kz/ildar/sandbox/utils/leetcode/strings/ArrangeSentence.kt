package kz.ildar.sandbox.utils.leetcode.strings

import java.util.*

/**
 * Arranges [sentence] in ascending word length order
 * time: O(NlogN) due to sorting
 * space: O(N)
 */
fun arrange(sentence: String): String {
    val sb = StringBuilder()
    var firstWord = true
    sentence.toLowerCase(Locale.US)
        .replace(".", "")
        .split(" ")
        .sortedBy { it.length }
        .forEach { word ->
            if (word.isEmpty()) return@forEach//skipping all empty words (due to double spaces)
            if (firstWord) {
                firstWord = false
                sb.append(word.substring(0, 1).toUpperCase(Locale.US))
                    .append(word.substring(1))
            } else {
                sb.append(" ").append(word)
            }
        }
    sb.append(".")
    return sb.toString()
}