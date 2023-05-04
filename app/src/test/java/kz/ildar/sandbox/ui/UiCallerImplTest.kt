package kz.ildar.sandbox.ui

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kz.ildar.sandbox.di.CoroutineProvider
import kz.ildar.sandbox.di.initTestKoin
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.koin.core.context.stopKoin

@OptIn(ExperimentalCoroutinesApi::class)
class UiCallerImplTest {
    private lateinit var uiCaller: UiCallerImpl
    private val testScope = CoroutineScope(Dispatchers.Unconfined)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        initTestKoin()
        uiCaller = UiCallerImpl(testScope, CoroutineProvider(), MutableLiveData(), MutableLiveData())
    }

    @After
    fun tearDown() = stopKoin()

    @Test
    fun `test error flow`() = runTest(dispatchTimeoutMs = 5000) {
        var errorCount = 0
        uiCaller.errorFlow.collect() {
            errorCount++
        }
        uiCaller.makeRequest({}) { throw Exception("test exception") }
        assertEquals(1, errorCount)
    }

    @Test
    fun `test loading state`() = runTest(dispatchTimeoutMs = 5000) {
        val loadingStates = mutableListOf<Boolean>()
        uiCaller.loadingState.take(1).collect { loadingStates.add(it) }
        uiCaller.makeRequest({}) { delay(1000) }
        assertEquals(listOf(true, false), loadingStates)
    }

    @Test
    fun `test success`() = runTest(dispatchTimeoutMs = 5000) {
        val a = uiCaller.errorFlow.firstOrNull()

        uiCaller.makeRequest({}) { }
        assertEquals(null, a)
    }
}