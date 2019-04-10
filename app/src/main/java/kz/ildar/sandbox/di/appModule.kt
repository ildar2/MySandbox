/**
 * (C) Copyright 2019 Ildar Ishalin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package kz.ildar.sandbox.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kz.ildar.sandbox.data.*
import kz.ildar.sandbox.data.api.Api
import kz.ildar.sandbox.ui.main.MainViewModel
import kz.ildar.sandbox.ui.main.child.ChildViewModel
import kz.ildar.sandbox.ui.main.color.ColorViewModel
import kz.ildar.sandbox.ui.main.hello.HelloViewModel
import kz.ildar.sandbox.ui.main.list.ColorListViewModel
import kz.ildar.sandbox.ui.main.multiCall.MultiCallViewModel
import kz.ildar.sandbox.ui.main.motion.MotionViewModel
import kz.ildar.sandbox.ui.main.websocket.WebsocketViewModel
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val appModule = module {
    factory { createOkHttp() }

    single { createApi(get()) }

    single { createRequest() }

    single<HelloRepository> { HelloRepositoryImpl(get()) }
    single { MultiCallRepository(get()) }
    single { FlatMapRepository(get()) }
    single { ColorRepository() }

    single { CoroutineProvider() }

    viewModel { MainViewModel() }
    viewModel { ChildViewModel(get()) }
    viewModel { HelloViewModel(get(), get()) }
    viewModel { WebsocketViewModel(get(), get(), get()) }
    viewModel { MotionViewModel() }
    viewModel { MultiCallViewModel(get(), get()) }
    viewModel { ColorViewModel(get(), get()) }
    viewModel { ColorListViewModel(get()) }
}

const val TIMEOUT = 3L

private fun createOkHttp(): OkHttpClient {
    return OkHttpClient.Builder()
        .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()
}

private fun createRequest(): Request {
    return Request.Builder()
        .url("ws://echo.websocket.org")
        .build()
}

private fun createApi(client: OkHttpClient): Api {
    return Retrofit.Builder()
//        .baseUrl("http://192.168.1.42:8080/")
        .baseUrl("https://postman-echo.com")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(Api::class.java)
}