package kz.ildar.sandbox.ui.main.color

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_color.*
import kotlinx.android.synthetic.main.include_toolbar.*
import kz.ildar.sandbox.R
import org.koin.android.viewmodel.ext.android.getViewModel
import android.R.attr.label
import android.content.ClipData
import android.content.Context.CLIPBOARD_SERVICE
import androidx.core.content.ContextCompat.getSystemService
import android.content.ClipboardManager
import android.content.Context

class ColorFragment : Fragment() {
    companion object {
        const val EXTRA_COLOR = "extra.color"
    }

    private lateinit var viewModel: ColorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel()
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_color, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar?.title = "Color picker"

        initSeekers()
        initPlusMinus()

        hexView.setOnLongClickListener {
            viewModel.colorLiveData.value?.let {
                val clipboard = context?.getSystemService(CLIPBOARD_SERVICE) as? ClipboardManager
                val clip = ClipData.newPlainText("Скопировано в буфер обмена", it.getHexString())
                clipboard?.primaryClip = clip
                Toast.makeText(
                    context,
                    "Скопировано в буфер обмена",
                    Toast.LENGTH_SHORT
                ).show()
            }
            true
        }
    }

    private val seekBarListener = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            if (fromUser) {
                when (seekBar) {
                    alphaSeekbar -> viewModel.setAlpha(progress)
                    redSeekbar -> viewModel.setRed(progress)
                    greenSeekbar -> viewModel.setGreen(progress)
                    blueSeekbar -> viewModel.setBlue(progress)
                }
            }
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
        }
    }

    private fun initSeekers() {
        alphaSeekbar.setOnSeekBarChangeListener(seekBarListener)
        redSeekbar.setOnSeekBarChangeListener(seekBarListener)
        greenSeekbar.setOnSeekBarChangeListener(seekBarListener)
        blueSeekbar.setOnSeekBarChangeListener(seekBarListener)
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