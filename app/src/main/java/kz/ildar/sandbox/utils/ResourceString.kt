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