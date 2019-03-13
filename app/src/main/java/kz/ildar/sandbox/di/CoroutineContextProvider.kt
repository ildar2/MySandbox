package kz.ildar.sandbox.di

import kotlinx.coroutines.Dispatchers
import kz.ildar.sandbox.ui.BaseViewModel
import kotlin.coroutines.CoroutineContext

/**
 * Used in [BaseViewModel] to make coroutine scope
 * should be mocked in tests (see [WebsocketViewModelTest])
 */
open class CoroutineContextProvider {
    open val main: CoroutineContext by lazy { Dispatchers.Main }
    open val io: CoroutineContext by lazy { Dispatchers.IO }
}