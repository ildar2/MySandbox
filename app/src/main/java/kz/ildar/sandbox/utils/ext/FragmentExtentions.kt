package kz.ildar.sandbox.utils.ext

import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import kz.ildar.sandbox.utils.EventObserver
import kz.ildar.sandbox.utils.EventWrapper
import kz.ildar.sandbox.utils.ResourceString

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

fun <T> Fragment.observe(liveData: LiveData<T>?, block: (T) -> Unit) {
    liveData?.observe(viewLifecycleOwner, Observer(block))
}

fun <T> Fragment.observeEvent(liveData: LiveData<EventWrapper<T>>, block: (T) -> Unit) {
    liveData.observe(viewLifecycleOwner, EventObserver(block))
}

fun showSnackBar(view: View?, message: String, long: Boolean = false) {
    val snackBar = Snackbar.make(
        view ?: return,
        message,
        if (long) Snackbar.LENGTH_INDEFINITE else Snackbar.LENGTH_LONG
    )
    (snackBar.view.findViewById<View>(com.google.android.material.R.id.snackbar_text) as? TextView)
        ?.maxLines = 5
    if (long) {
        snackBar.setAction("ОК") {
            snackBar.dismiss()
        }
    }
    snackBar.show()
}
