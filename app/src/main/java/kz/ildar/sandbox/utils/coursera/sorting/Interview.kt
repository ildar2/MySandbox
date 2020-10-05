package kz.ildar.sandbox.utils.coursera.sorting

/**
 * Nuts and bolts.
 *
 * A disorganized carpenter has a mixed pile of n nuts and n bolts.
 * The goal is to find the corresponding pairs of nuts and bolts.
 * Each nut fits exactly one bolt and each bolt fits exactly one nut.
 *
 * By fitting a nut and a bolt together, the carpenter can see which one is
 * bigger (but the carpenter cannot compare two nuts or two bolts directly).
 *
 * Design an algorithm for the problem that uses at most proportional
 * to n logn compares (probabilistically).
 *
 * [2, 4, 1, 3] - nuts
 * [30, 20, 40, 10] - bolts
 *
 * we can partition bolts based on each nut
 * for 2: [10, 20, 40, 30] i=1
 * for 4: [10, 20, 30, 40] i=3
 */
fun nutsBolts(nuts: IntArray, bolts: IntArray): IntArray {
    assert(nuts.size == bolts.size) { "should be of same size" }


    return nuts
}

/**
 * Selection in two sorted arrays.
 *
 * Given two sorted arrays a[] and b[], of lengths n1 and n2 and an
 * integer 0≤k<n1+n2, design an algorithm to find a key of rank k.
 *
 * The order of growth of the worst case running time
 * of your algorithm should be nlogn, where n=n1+n2.
 *
 *  Version 1: n1=n2 (equal length arrays) and k=n/2 (median).
 *  Version 2: k=n/2 (median).
 *  Version 3: no restrictions.
 *
 *  [1, 5, 7, 9, 12]
 *  [2, 4, 8, 10, 11]
 *  k = 5
 *  output: 7 (because 7 is 5th smallest number in both arrays)
 *  k = 3
 *  output: 4
 *  k = 8
 *  output: 10
 */
fun rankInTwoArrays(a: IntArray, b: IntArray, k: Int): Int {

    return k
}

fun main() {
    println(nutsBolts(intArrayOf(2, 1, 3), intArrayOf(3, 2, 1)).contentToString())
}