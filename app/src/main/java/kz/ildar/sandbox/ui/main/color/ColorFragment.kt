package kz.ildar.sandbox.ui.main.color

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_color.*
import kotlinx.android.synthetic.main.include_toolbar.*
import kz.ildar.sandbox.R
import kz.ildar.sandbox.data.model.ColorModel
import org.koin.android.viewmodel.ext.android.getViewModel

class ColorFragment : Fragment() {

    private lateinit var viewModel: ColorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel()
        if (savedInstanceState == null) {
            arguments?.getParcelable<ColorModel>("colorModel")?.run {
                viewModel.setAlpha(alpha)
                viewModel.setRed(red)
                viewModel.setGreen(green)
                viewModel.setBlue(blue)
            }
        }
        viewModel.alphaLiveData.observe(this, Observer {
            alphaSeekbar.progress = it
            alphaValue.text = it.toString()
            updateColor()
        })
        viewModel.redLiveData.observe(this, Observer {
            redSeekbar.progress = it
            redValue.text = it.toString()
            updateColor()
        })
        viewModel.greenLiveData.observe(this, Observer {
            greenSeekbar.progress = it
            greenValue.text = it.toString()
            updateColor()
        })
        viewModel.blueLiveData.observe(this, Observer {
            blueSeekbar.progress = it
            blueValue.text = it.toString()
            updateColor()
        })
    }

    private fun updateColor() {
        val alpha = viewModel.alphaLiveData.value ?: 0
        val red = viewModel.redLiveData.value ?: 0
        val green = viewModel.greenLiveData.value ?: 0
        val blue = viewModel.blueLiveData.value ?: 0
        val color = Color.argb(alpha, red, green, blue)
        hexView.text = String.format("#%08X", color)
        colorView.setBackgroundColor(color)
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
    }

    val seekBarListener = object : SeekBar.OnSeekBarChangeListener {
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
    val AUTO_DELAY = 50L
    val autoPressHandler: Handler = Handler()

    internal inner class AutoPressEvent : Runnable {
        override fun run() {
            if (autoIncrement) {
                viewModel.plusClick()
                autoPressHandler.postDelayed(AutoPressEvent(), AUTO_DELAY)
            } else if (autoDecrement) {
                viewModel.minusClick()
                autoPressHandler.postDelayed(AutoPressEvent(), AUTO_DELAY)
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
        minusView.setOnTouchListener { v, event ->
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
        plusView.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP && autoIncrement) {
                autoIncrement = false
            }
            false
        }
    }
}