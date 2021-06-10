package kz.ildar.sandbox.utils.ext

import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun SeekBar.doOnProgressChanged(action: (Int) -> Unit) {
    setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            if (fromUser && seekBar === this@doOnProgressChanged) action(progress)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
            //do nothing
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
            //do nothing
        }
    })
}

fun View.show(show: Boolean = true) {
    if (show) visibility = View.VISIBLE
    else hide()
}

fun View.hide(gone: Boolean = true) {
    visibility = if (gone) View.GONE else View.INVISIBLE
}

fun TextView.textColorRes(@ColorRes color: Int) {
    setTextColor(ContextCompat.getColor(context, color))
}
