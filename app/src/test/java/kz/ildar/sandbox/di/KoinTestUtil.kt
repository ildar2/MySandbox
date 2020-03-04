package kz.ildar.sandbox.di

import androidx.lifecycle.MutableLiveData
import com.nhaarman.mockitokotlin2.spy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kz.ildar.sandbox.ui.UiCaller
import kz.ildar.sandbox.ui.UiCallerImpl
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

fun initTestKoin(vararg modules: Module) {
    startKoin {
        modules(
            listOf(
                module {
                    single<CoroutineContext>(named("io")) { Dispatchers.Unconfined }
                    single<CoroutineContext>(named("main")) { Dispatchers.Unconfined }
                    single<UiCaller> { spy(UiCallerImpl(CoroutineScope(Job()), CoroutineProvider(), MutableLiveData(), MutableLiveData())) }
                },
                *modules
            )
        )
    }
}