package kz.ildar.sandbox.di

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 * Used for mocking coroutine requests
 *
 * https://android.jlelse.eu/mastering-coroutines-android-unit-tests-8bc0d082bf15
 */
class TestContextProvider : CoroutineContextProvider() {
    override val main: CoroutineContext = Dispatchers.Unconfined
    override val io: CoroutineContext = Dispatchers.Unconfined
}