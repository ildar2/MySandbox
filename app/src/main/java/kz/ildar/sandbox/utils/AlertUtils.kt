package kz.ildar.sandbox.utils

import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.toast(
    message: String?,
    long: Boolean = false
) = Toast.makeText(
    context,
    message,
    if (long) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
).show()

fun Fragment.toast(
    message: ResourceString?,
    long: Boolean = false
) {
    toast(
        message?.format(context ?: return),
        long
    )
}