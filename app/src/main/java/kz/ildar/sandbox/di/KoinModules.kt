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

import android.content.Context
import android.hardware.SensorManager
import android.os.Vibrator
import kotlinx.coroutines.Dispatchers
import kz.ildar.sandbox.data.*
import kz.ildar.sandbox.data.api.Api
import kz.ildar.sandbox.ui.main.MainViewModel
import kz.ildar.sandbox.ui.main.child.ChildViewModel
import kz.ildar.sandbox.ui.main.color.ColorViewModel
import kz.ildar.sandbox.ui.main.hello.HelloViewModel
import kz.ildar.sandbox.ui.main.list.ColorListViewModel
import kz.ildar.sandbox.ui.main.memory.MemoryTrainingViewModel
import kz.ildar.sandbox.ui.main.sensor.SensorViewModel
import kz.ildar.sandbox.ui.main.sensor.SensorCallbacks
import kz.ildar.sandbox.ui.main.multiCall.MultiCallViewModel
import kz.ildar.sandbox.ui.main.playground.PlaygroundViewModel
import kz.ildar.sandbox.ui.main.rainbow.Rainbow2ViewModel
import kz.ildar.sandbox.ui.main.rainbow.RainbowViewModel
import kz.ildar.sandbox.ui.main.stories.StoriesViewModel
import kz.ildar.sandbox.ui.main.websocket.WebsocketViewModel
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

val appModule = module {
    factory { createOkHttp() }

    single { createApi(client = get()) }

    single { createRequest() }
    single {
        androidContext().getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }

    single<HelloRepository> { HelloRepositoryImpl(api = get()) }
    single { MultiCallRepository(api = get()) }
    single { ColorRepository() }
    single { sensorManager(context = androidContext()) }
    single { SensorCallbacks(sensorManager = get()) }

    single<CoroutineContext>(named(NAME_IO)) { Dispatchers.IO }
    single<CoroutineContext>(named(NAME_MAIN)) { Dispatchers.Main }

    viewModel { MainViewModel() }
    viewModel { StoriesViewModel() }
    viewModel { ChildViewModel(helloRepository = get()) }
    viewModel { RainbowViewModel(colorRepository = get()) }
    viewModel { Rainbow2ViewModel(colorRepository = get()) }
    viewModel { HelloViewModel(helloRepository = get()) }
    viewModel { WebsocketViewModel(client = get(), request = get()) }
    viewModel { SensorViewModel(sensorCallbacks = get()) }
    viewModel { PlaygroundViewModel(vibrator = get()) }
    viewModel { MemoryTrainingViewModel() }
    viewModel { MultiCallViewModel(multiRepo = get()) }
    viewModel { ColorViewModel(colorRepository = get()) }
    viewModel { ColorListViewModel(colorRepository = get()) }
}
const val TIMEOUT = 3L

private fun createOkHttp(): OkHttpClient = OkHttpClient.Builder()
    .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
    .readTimeout(TIMEOUT, TimeUnit.SECONDS)
    .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
    .build()

private fun createRequest(): Request = Request.Builder()
    .url("ws://echo.websocket.org")
    .build()

private fun createApi(client: OkHttpClient): Api = Retrofit.Builder()
//    .baseUrl("http://192.168.1.42:8080/")
    .baseUrl("https://postman-echo.com")
    .client(client)
    .addCallAdapterFactory(GoApiCallAdapterFactory())
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(Api::class.java)

private fun sensorManager(
    context: Context
): SensorManager? = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager

const val NAME_IO = "io"
const val NAME_MAIN = "main"
