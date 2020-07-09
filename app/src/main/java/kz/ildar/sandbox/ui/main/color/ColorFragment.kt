package kz.ildar.sandbox.ui.main.color

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_color.*
import kotlinx.android.synthetic.main.include_toolbar.*
import kz.ildar.sandbox.R
import kz.ildar.sandbox.utils.doOnProgressChanged
import kz.ildar.sandbox.utils.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class ColorFragment : Fragment(R.layout.fragment_color) {
    companion object {
        const val EXTRA_COLOR = "extra.color"
    }

    private val viewModel: ColorViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getParcelable<ColorMutable>(EXTRA_COLOR)?.let {
            if (savedInstanceState == null) {
                viewModel.initColor(it)
            }
        }
        viewModel.colorLiveData.observe(this, Observer {
            updateColor(it)
        })
    }

    private fun updateColor(colorModel: ColorMutable) {
        with(colorModel) {
            if (!name.isBlank()) toolbar.title = name
            colorView.setBackgroundColor(getColor())
            hexView.text = getHexString()

            alphaSeekbar.progress = alpha
            alphaValue.text = alpha.toString()
            redSeekbar.progress = red
            redValue.text = red.toString()
            greenSeekbar.progress = green
            greenValue.text = green.toString()
            blueSeekbar.progress = blue
            blueValue.text = blue.toString()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        toolbar.title = getString(R.string.color_picker_title)

        initSeekers()
        initPlusMinus()

        hexView.setOnLongClickListener {
            viewModel.colorLiveData.value?.let {
                val clipboard = context?.getSystemService(CLIPBOARD_SERVICE) as? ClipboardManager
                val clip = ClipData.newPlainText(getString(R.string.copied_to_clipboard), it.getHexString())
                clipboard?.setPrimaryClip(clip)
                toast(getString(R.string.copied_to_clipboard))
            }
            true
        }
    }

    private fun initSeekers() {
        alphaSeekbar.doOnProgressChanged { viewModel.setAlpha(it) }
        redSeekbar.doOnProgressChanged { viewModel.setRed(it) }
        greenSeekbar.doOnProgressChanged { viewModel.setGreen(it) }
        blueSeekbar.doOnProgressChanged { viewModel.setBlue(it) }
    }

    var autoIncrement = false
    var autoDecrement = false
    val delay = 50L
    val autoPressHandler: Handler = Handler()

    internal inner class AutoPressEvent : Runnable {
        override fun run() {
            if (autoIncrement) {
                viewModel.plusClick()
                autoPressHandler.postDelayed(AutoPressEvent(), delay)
            } else if (autoDecrement) {
                viewModel.minusClick()
                autoPressHandler.postDelayed(AutoPressEvent(), delay)
            }
        }
    }

    private fun initPlusMinus() {
        minusView.setOnClickListener { viewModel.minusClick() }
        minusView.setOnLongClickListener {
            autoDecrement = true
            autoPressHandler.post(AutoPressEvent())
            true
        }
        minusView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP && autoDecrement) {
                autoDecrement = false
            }
            false
        }

        plusView.setOnClickListener { viewModel.plusClick() }
        plusView.setOnLongClickListener {
            autoIncrement = true
            autoPressHandler.post(AutoPressEvent())
            true
        }
        plusView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP && autoIncrement) {
                autoIncrement = false
            }
            false
        }
    }
}