package kz.ildar.sandbox.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kz.ildar.sandbox.data.HelloRepository
import kz.ildar.sandbox.data.HelloRepositoryImpl
import kz.ildar.sandbox.data.api.Api
import kz.ildar.sandbox.ui.main.MainViewModel
import kz.ildar.sandbox.ui.main.child.ChildViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val appModule = module {
    factory { createOkHttp() }

    single { createApi(get()) }

    single<HelloRepository> { HelloRepositoryImpl(get()) }

    viewModel { MainViewModel(get()) }
    viewModel { ChildViewModel(get()) }
}

const val TIMEOUT = 5L

private fun createOkHttp(): OkHttpClient {
    return OkHttpClient.Builder()
        .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()
}

private fun createApi(client: OkHttpClient): Api {
    return Retrofit.Builder()
        .baseUrl("http://192.168.1.42:8080/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(Api::class.java)
}