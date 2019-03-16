package kz.ildar.sandbox.ui.main.color

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_color.*
import kotlinx.android.synthetic.main.include_toolbar.*
import kz.ildar.sandbox.R
import org.koin.android.viewmodel.ext.android.getViewModel

class ColorFragment : Fragment() {

    private lateinit var viewModel: ColorViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = getViewModel()

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
        hexView.text = "#" + Integer.toHexString(color).toUpperCase()
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

        alphaSeekbar.setOnSeekBarChangeListener(seekBarListener)
        redSeekbar.setOnSeekBarChangeListener(seekBarListener)
        greenSeekbar.setOnSeekBarChangeListener(seekBarListener)
        blueSeekbar.setOnSeekBarChangeListener(seekBarListener)
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
}