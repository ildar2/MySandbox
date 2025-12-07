package kz.ildar.sandbox.ui.main.color

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kz.ildar.sandbox.R
import kz.ildar.sandbox.databinding.FragmentColorBinding
import kz.ildar.sandbox.utils.ext.doOnProgressChanged
import kz.ildar.sandbox.utils.ext.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class ColorFragment : Fragment(R.layout.fragment_color) {
    companion object {
        const val EXTRA_COLOR = "extra.color"
    }

    private val viewModel: ColorViewModel by viewModel()
    private lateinit var binding: FragmentColorBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentColorBinding.inflate(inflater, container, false)
        return binding.root
    }

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
            if (!name.isBlank()) binding.include?.toolbar?.title = name
            binding.colorView.setBackgroundColor(getColor())
            binding.hexView.text = getHexString()

            binding.alphaSeekbar.progress = alpha
            binding.alphaValue.text = alpha.toString()
            binding.redSeekbar.progress = red
            binding.redValue.text = red.toString()
            binding.greenSeekbar.progress = green
            binding.greenValue.text = green.toString()
            binding.blueSeekbar.progress = blue
            binding.blueValue.text = blue.toString()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.include?.toolbar?.setNavigationIcon(R.drawable.ic_arrow_back)
        binding.include?.toolbar?.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        binding.include?.toolbar?.title = getString(R.string.color_picker_title)

        initSeekers()
        initPlusMinus()

        binding.hexView.setOnLongClickListener {
            viewModel.colorLiveData.value?.let {
                val clipboard = context?.getSystemService(CLIPBOARD_SERVICE) as? ClipboardManager
                val clip = ClipData.newPlainText(
                    getString(R.string.copied_to_clipboard),
                    it.getHexString()
                )
                clipboard?.setPrimaryClip(clip)
                toast(getString(R.string.copied_to_clipboard))
            }
            true
        }
    }

    private fun initSeekers() {
        binding.alphaSeekbar.doOnProgressChanged { viewModel.setAlpha(it) }
        binding.redSeekbar.doOnProgressChanged { viewModel.setRed(it) }
        binding.greenSeekbar.doOnProgressChanged { viewModel.setGreen(it) }
        binding.blueSeekbar.doOnProgressChanged { viewModel.setBlue(it) }
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
        binding.minusView.setOnClickListener { viewModel.minusClick() }
        binding.minusView.setOnLongClickListener {
            autoDecrement = true
            autoPressHandler.post(AutoPressEvent())
            true
        }
        binding.minusView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP && autoDecrement) {
                autoDecrement = false
            }
            false
        }

        binding.plusView.setOnClickListener { viewModel.plusClick() }
        binding.plusView.setOnLongClickListener {
            autoIncrement = true
            autoPressHandler.post(AutoPressEvent())
            true
        }
        binding.plusView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP && autoIncrement) {
                autoIncrement = false
            }
            false
        }
    }
}