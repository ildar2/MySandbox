package kz.ildar.sandbox.utils

import android.os.SystemClock
import android.view.View

class SafeClickListener(
    private var defaultInterval: Int = 1000,
    private val action: (View) -> Unit
) : View.OnClickListener {

    private var lastTimeClicked: Long = 0

    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) {
            return
        }
        lastTimeClicked = SystemClock.elapsedRealtime()
        action(v)
    }
}

/**
 * Prevent double click
 */
fun View.setSafeOnClickListener(action: (View) -> Unit) {
    setOnClickListener(SafeClickListener {
        action(it)
    })
}
