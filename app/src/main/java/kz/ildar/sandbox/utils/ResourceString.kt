package kz.ildar.sandbox.utils

import android.content.Context

/**
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

class FormatResourceString(private val id: Int, val values: Array<Any>) : ResourceString() {
    override fun format(context: Context): String = context.getString(id, *values)
}