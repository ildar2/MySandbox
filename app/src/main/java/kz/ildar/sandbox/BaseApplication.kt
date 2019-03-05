package kz.ildar.sandbox

import android.app.Application
import kz.ildar.sandbox.di.appModule
import org.koin.android.ext.android.startKoin
import timber.log.Timber

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(appModule))
        Timber.plant(Timber.DebugTree())
    }
}