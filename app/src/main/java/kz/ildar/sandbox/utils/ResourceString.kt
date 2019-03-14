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

import android.content.Context

/**
 * Object for user-messages outside fragments, since there should be no [Context] references
 *
 * Got from https://code.luasoftware.com/tutorials/android/android-use-livedata-to-show-toast-message-from-viewmodel/
 */
sealed class ResourceString {
    abstract fun format(context: Context): String
}

class IdResourceString(private val id: Int) : ResourceString() {
    override fun format(context: Context): String = context.getString(id)
}

class TextResourceString(private val text: String?) : ResourceString() {
    override fun format(context: Context): String = text ?: ""
}

class FormatResourceString(private val id: Int, vararg val args: Any) : ResourceString() {
    override fun format(context: Context): String = context.getString(id, *args)
}