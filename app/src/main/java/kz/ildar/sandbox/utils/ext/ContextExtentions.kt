package kz.ildar.sandbox.utils.ext

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kz.ildar.sandbox.utils.EventObserver
import kz.ildar.sandbox.utils.EventWrapper

fun Context.toast(
    message: CharSequence?,
    isLong: Boolean = false
) = Toast.makeText(
    this,
    message,
    if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
).show()

fun <T> FragmentActivity.observe(liveData: LiveData<T>?, block: (T) -> Unit) {
    liveData?.observe(this, Observer(block))
}

fun <T> FragmentActivity.observeEvent(liveData: LiveData<EventWrapper<T>>, block: (T) -> Unit) {
    liveData.observe(this, EventObserver(block))
}
