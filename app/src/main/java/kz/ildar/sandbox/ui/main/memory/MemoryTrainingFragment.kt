/**
 * (C) Copyright 2021 Ildar Ishalin
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
package kz.ildar.sandbox.ui.main.memory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kz.ildar.sandbox.R
import kz.ildar.sandbox.databinding.FragmentMemoryTrainingBinding
import kz.ildar.sandbox.utils.DisplayAdapter
import kz.ildar.sandbox.utils.ext.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MemoryTrainingFragment : Fragment(R.layout.fragment_memory_training) {

    val viewModel: MemoryTrainingViewModel by viewModel()
    private val adapter = object : DisplayAdapter() {
        override fun createViewHolder(view: View, viewType: Int) = PointDisplay.PointViewHolder(view)
    }
    private lateinit var binding: FragmentMemoryTrainingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMemoryTrainingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.difficultyPicker.minValue = 3
        binding.difficultyPicker.maxValue = 16
        binding.difficultyPicker.value = 6
        binding.button.setOnClickListener {
            viewModel.start(binding.difficultyPicker.value)
        }
        observe(viewModel.pointLiveData) {
            adapter.items = it
        }
        observeEvent(viewModel.pointUpdateEvent) {
            adapter.notifyItemChanged(it)
        }
        observe(viewModel.stateLiveData) {
            binding.header.text = it.name
            when (it) {
                TrainingState.INIT -> {
                    binding.difficultyPicker.show()
                    binding.button.show()
                    binding.button.text = getString(R.string.memory_button_start)
                    binding.header.show()
                    binding.header.text = getString(R.string.memory_tooltip_init)
                }
                TrainingState.PRODUCING -> {
                    binding.difficultyPicker.hide()
                    binding.button.hide()
                    binding.header.hide()
                }
                TrainingState.RECEIVING -> {
                    binding.difficultyPicker.hide()
                    binding.button.hide()
                    binding.header.show()
                    binding.header.text = getString(R.string.memory_tooltip_sequence)
                }
                TrainingState.ERROR -> {
                    binding.difficultyPicker.show()
                    binding.button.show()
                    binding.button.text = getString(R.string.memory_button_retry)
                    binding.header.show()
                    binding.header.text = getString(R.string.memory_tooltip_error)
                }
                TrainingState.END -> {
                    binding.difficultyPicker.hide()
                    binding.button.hide()
                    binding.header.show()
                    binding.header.text = getString(R.string.memory_tooltip_success)
                }
            }
        }
        binding.rvPoints.adapter = adapter
        observeEvent(viewModel.errorLiveData) {
            toast(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvPoints.adapter = null
    }
}
