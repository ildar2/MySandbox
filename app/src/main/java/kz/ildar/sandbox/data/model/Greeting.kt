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
package kz.ildar.sandbox.data.model

interface GreetingsResponse {
    fun getContents(): String
}

/**
 * Postman echo model
 */
data class GreetingWrapper(private val args: Greeting, private val url: String) : GreetingsResponse by args

/**
 * Local server model
 */
data class Greeting(private val id: Long, private val content: String) : GreetingsResponse {
    override fun getContents() = content
}