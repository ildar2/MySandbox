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
package kz.ildar.sandbox.utils

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun foo(callback: (() -> Unit)? = {
    System.out.println("Default callback called")
}) {
    System.out.println("foo called")
    callback?.let {
        System.out.println("--with callback")
        it.invoke()
    }
}

fun fooCaller() {
    System.out.println("Calling foo with null callback")
    foo(null)
    System.out.println()
    System.out.println("Calling foo with no callback")
    foo()
    System.out.println()
    System.out.println("Calling foo with callback")
    foo {
        System.out.println("Executing callback")
    }
}

fun main() = runBlocking {
    refFuncCaller()
}

fun refFunc(arg: String, func: (String) -> Unit) {
    System.out.println("Executing refFunc")
    func(arg)
}

fun handler(text: String) = runBlocking {
    delay(600)
    System.out.println("Executing handler: $text")
}

fun refFuncCaller() {
    refFunc("text1", ::handler)
    refFunc("text2", ::handler)
}

