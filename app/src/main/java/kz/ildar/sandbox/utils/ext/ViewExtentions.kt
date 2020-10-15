package kz.ildar.sandbox.utils.ext

import android.widget.SeekBar

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
