package kz.ildar.sandbox.di

import androidx.lifecycle.MutableLiveData
import com.nhaarman.mockitokotlin2.spy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kz.ildar.sandbox.ui.UiCaller
import kz.ildar.sandbox.ui.UiCallerImpl
import org.koin.dsl.module.Module
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext
import kotlin.coroutines.CoroutineContext

fun initTestKoin(vararg modules: Module) {
    StandAloneContext.startKoin(//https://proandroiddev.com/testing-with-koin-ade8a46eb4d
        listOf(
            module {
                single<CoroutineContext>("io") { Dispatchers.Unconfined }
                single<CoroutineContext>("main") { Dispatchers.Unconfined }
                single<UiCaller> { spy(UiCallerImpl(CoroutineScope(Job()), CoroutineProvider(), MutableLiveData(), MutableLiveData())) }
            },
            *modules
        )
    )
}