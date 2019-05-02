package kz.ildar.sandbox.di

import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module.Module
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext
import kotlin.coroutines.CoroutineContext

fun initTestKoin(vararg modules: Module) {
    StandAloneContext.startKoin(//https://proandroiddev.com/testing-with-koin-ade8a46eb4d
        listOf(
            module {
                single<CoroutineContext>("io") { Dispatchers.Unconfined }
                single<CoroutineContext> { Dispatchers.Unconfined }
            },
            *modules
        )
    )
}