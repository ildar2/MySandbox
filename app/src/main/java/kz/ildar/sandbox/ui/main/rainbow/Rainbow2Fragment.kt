/**
 * (C) Copyright 2019 Ildar Ishalin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package kz.ildar.sandbox.ui.main.rainbow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kz.ildar.sandbox.R
import kz.ildar.sandbox.databinding.FragmentRainbow2Binding
import kz.ildar.sandbox.ui.Status
import kz.ildar.sandbox.utils.ext.hide
import kz.ildar.sandbox.utils.ext.observe
import kz.ildar.sandbox.utils.ext.show
import kz.ildar.sandbox.utils.ext.textColorRes
import kz.ildar.sandbox.utils.setSafeOnClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Тренажер Цветовод (Florist)
 * совпадает ли название цвета наверху с цветом текста внизу?
 *
 * todo:
 *  group views
 *  countdown
 *  pretty buttons
 *  result screen
 */
class Rainbow2Fragment : Fragment(R.layout.fragment_rainbow_2) {

    private val viewModel: Rainbow2ViewModel by viewModel()
    private lateinit var binding: FragmentRainbow2Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRainbow2Binding.inflate(inflater, container, false)
        return binding.root
    }

    private fun initViewModel() {
        observe(viewModel.rainbowItemLiveData) { item ->
            binding.tvResult.text = "Совпадает ли название цвета наверху с цветом текста внизу?"
            binding.tvResult.textColorRes(R.color.gray_solid)
            binding.rainbowView.text = item.text1
            binding.rainbowView.setTextColor(item.textColor1)
            binding.rainbow2View.text = item.text2
            binding.rainbow2View.setTextColor(item.textColor2)
        }
        observe(viewModel.statusLiveData) {
            when (it) {
                Status.SHOW_LOADING -> {
                    binding.tvResult.show()
                    binding.startView.hide()
                    binding.minutePickerLabel.hide()
                    binding.minutePicker.hide()
                    binding.rainbowView.show()
                    binding.rainbow2View.show()
                    binding.bYes.show()
                    binding.bNo.show()
                }
                Status.HIDE_LOADING -> {
                    binding.tvResult.hide()
                    binding.startView.show()
                    binding.minutePickerLabel.show()
                    binding.minutePicker.show()
                    binding.rainbowView.hide()
                    binding.rainbow2View.hide()
                    binding.bYes.hide()
                    binding.bNo.hide()
                }
                Status.SUCCESS -> {
                    binding.tvResult.show()
                    binding.tvResult.text = "Правильно!"
                    binding.tvResult.textColorRes(R.color.success)
                }
                else -> {
                    binding.tvResult.show()
                    binding.tvResult.text = "Неправильно!"
                    binding.tvResult.textColorRes(R.color.error)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViewModel()
        binding.include.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        binding.include.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        binding.include.toolbar.hide()

        binding.minutePicker.minValue = 1
        binding.minutePicker.maxValue = 10

        binding.startView.setOnClickListener {
            viewModel.start(terminate = binding.minutePicker.value * 60000L)
        }
        binding.contentView.setOnClickListener {
            viewModel.stop()
        }
        binding.bYes.setSafeOnClickListener {
            viewModel.answerYes()
        }
        binding.bNo.setSafeOnClickListener {
            viewModel.answerNo()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.stop()
    }
}