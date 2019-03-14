/**
 * (C) Copyright 2018 Paulo Vitor Sato Open Source Project
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
package kz.ildar.sandbox.utils;

import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlin.coroutines.CoroutineContext

/**
 * Abstract class for a Use Case (Interactor in terms of Clean Architecture).
 * This interface represents a execution unit for different use cases (this means any use case
 * in the application should implement this contract).
 * <p>
 * By convention each UseCase implementation will return the result using a coroutine
 * that will execute its job in a background thread and will post the result in the UI thread.
 */
abstract class UseCase<T> {

    protected var parentJob: Job = Job()
    //var backgroundContext: CoroutineContext = IO
    var backgroundContext: CoroutineContext = IO
    var foregroundContext: CoroutineContext = Dispatchers.Main
    var scope: CoroutineScope = CoroutineScope(parentJob + backgroundContext)


    protected abstract suspend fun executeOnBackground(): T

    fun execute(onComplete: (T) -> Unit, onError: (Throwable) -> Unit) {
        parentJob.cancel()
        parentJob = Job()


        scope.launch(foregroundContext) {
            try {
                val result = withContext(backgroundContext) {
                    executeOnBackground()
                }
                onComplete.invoke(result)
            } catch (e: CancellationException) {
                Log.d("UseCase", "canceled by user")
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    protected suspend fun <X> background(context: CoroutineContext = backgroundContext, block: suspend () -> X): Deferred<X> {
        return scope.async(context) {
            block.invoke()
        }
    }

    fun unsubscribe() {
        parentJob.cancel()
    }

}