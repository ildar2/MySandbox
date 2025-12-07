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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kz.ildar.sandbox.R
import kz.ildar.sandbox.databinding.FragmentRainbowBinding
import kz.ildar.sandbox.ui.Status
import kz.ildar.sandbox.utils.ext.hide
import kz.ildar.sandbox.utils.ext.show
import kz.ildar.sandbox.utils.ext.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class RainbowFragment : Fragment(R.layout.fragment_rainbow) {

    private val viewModel: RainbowViewModel by viewModel()
    private lateinit var binding: FragmentRainbowBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRainbowBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun initViewModel() {
        viewModel.rainbowItemLiveData.observe(viewLifecycleOwner, Observer { item ->
            val params = binding.rainbowView.layoutParams as ConstraintLayout.LayoutParams
            params.horizontalBias = item.x
            params.verticalBias = item.y
            binding.rainbowView.layoutParams = params
            binding.rainbowView.text = item.text
            binding.rainbowView.setTextColor(item.textColor)
            binding.rainbowView.setBackgroundColor(item.bgColor)
        })
        viewModel.statusLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                Status.SHOW_LOADING -> {
                    binding.tooltip.hide()
                    binding.startView.hide()
                    binding.minutePickerLabel.hide()
                    binding.minutePicker.hide()
                    binding.rainbowView.show()
                }

                Status.HIDE_LOADING -> {
                    binding.tooltip.show()
                    binding.startView.show()
                    binding.minutePickerLabel.show()
                    binding.minutePicker.show()
                    binding.rainbowView.hide()
                }

                Status.SUCCESS -> {
                    toast(getString(R.string.rainbow_finish))
                }

                else -> {
                    //do nothing
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViewModel()
        binding.toolbar.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        binding.toolbar.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        binding.toolbar.toolbar.hide()

        binding.minutePicker.minValue = 1
        binding.minutePicker.maxValue = 10

        binding.startView.setOnClickListener {
            viewModel.start(terminate = binding.minutePicker.value * 60000L)
        }
        binding.contentView.setOnClickListener {
            viewModel.stop()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.stop()
    }
}