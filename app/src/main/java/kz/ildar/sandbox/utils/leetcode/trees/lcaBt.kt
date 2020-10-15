package kz.ildar.sandbox.utils.leetcode.trees

import android.util.Log
import android.view.View
import kotlin.reflect.KClass
import kotlin.reflect.KClassifier

class Node<T>(
    val value: T,
    var left: Node<T>? = null,
    var right: Node<T>? = null
) {
    override fun toString() = "[$value]"
    override fun equals(other: Any?) = other is Node<*> && other.value == value
    override fun hashCode() = value.hashCode()
}

fun <T> lcaBt(root: Node<T>?, n1: Node<T>, n2: Node<T>): Node<T>? {
    if (root == null) return null
    if (root == n1 || root == n2) return root//might be ===
    val left = lcaBt(root.left, n1, n2)
    val right = lcaBt(root.right, n1, n2)
    if (left != null && right != null) return root
    return left ?: right
}

fun main() {

    println()
}

/**
 * can't properly retrieve supertypes, because supertypes returns both classes and interfaces
 */
inline fun <reified T> lcaViewsKotlin(v1: T, v2: T): KClass<*> where T : View {
    val set = mutableSetOf<KClassifier>()
    addAncestorsRec(set, v1::class)
    Log.w("lcaViews", "$set")
    return checkAncestorsRec(set, v2::class)
        ?: throw IllegalArgumentException("there are no common ancestors between ${v1::class} and ${v2::class}")
}

/**
 * recursively check all supertypes's supertypes
 * taking only first, because we don't need interfaces
 * bug: the top class (subclass of Any) with interfaces checks first interface
 */
fun checkAncestorsRec(set: MutableSet<KClassifier>, v2Class: KClass<*>?): KClass<*>? {
    v2Class ?: return null
    if (set.contains(v2Class)) {
        return v2Class
    }
    return v2Class.supertypes.firstOrNull()?.let {
        checkAncestorsRec(set, it.classifier as? KClass<*>)
    }
}

fun addAncestorsRec(set: MutableSet<KClassifier>, v1Class: KClass<*>?) {
    v1Class ?: return
    Log.w("lcaViews", "adding $v1Class, is has supertypes: ${v1Class.supertypes}")
    set.add(v1Class)
    v1Class.supertypes.firstOrNull()?.let {
        addAncestorsRec(set, it.classifier as? KClass<*>)
    }
}

/**
 * return least common ancestor for two views
 */
inline fun <reified T> lcaViews(v1: T, v2: T): Class<*> where T : View {
    val set = mutableSetOf<Class<*>>()
    var cls: Class<*>? = v1::class.java
    while(cls != null) {
        set.add(cls)
        cls = cls.superclass
    }

    cls = v2::class.java
    while(cls != null) {
        if (set.contains(cls)) return cls
        cls = cls.superclass
    }

    throw IllegalArgumentException("there are no common ancestors between ${v1::class} and ${v2::class}")
}

