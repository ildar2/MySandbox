package kz.ildar.sandbox.utils.ext

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract


/**
 * Выполняет [block] только для непустых строк
 */
@OptIn(ExperimentalContracts::class)
inline fun CharSequence?.notBlank(block: (String) -> Unit): Boolean {
    contract {
        returns(true) implies (this@notBlank != null)
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    return if (!this.isNullOrBlank()) {
        block(this.toString())
        true
    } else false
}

fun <T : Number> T?.orZero(): T = this ?: orDefault(0 as T)

fun <T : Number> T?.orDefault(default: T): T = this ?: default
