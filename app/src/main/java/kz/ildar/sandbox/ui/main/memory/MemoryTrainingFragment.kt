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
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_memory_training.*
import kz.ildar.sandbox.R
import kz.ildar.sandbox.utils.DisplayAdapter
import kz.ildar.sandbox.utils.ext.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MemoryTrainingFragment : Fragment(R.layout.fragment_memory_training) {

    val viewModel: MemoryTrainingViewModel by viewModel()
    private val adapter = object : DisplayAdapter() {
        override fun createViewHolder(view: View, viewType: Int) = PointDisplay.PointViewHolder(view)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        difficultyPicker.minValue = 3
        difficultyPicker.maxValue = 16
        difficultyPicker.value = 6
        button.setOnClickListener {
            viewModel.start(difficultyPicker.value)
        }
        observe(viewModel.pointLiveData) {
            adapter.items = it
        }
        observeEvent(viewModel.pointUpdateEvent) {
            adapter.notifyItemChanged(it)
        }
        observe(viewModel.stateLiveData) {
            header.text = it.name
            when (it) {
                TrainingState.INIT -> {
                    difficultyPicker.show()
                    button.show()
                    button.text = getString(R.string.memory_button_start)
                    header.show()
                    header.text = getString(R.string.memory_tooltip_init)
                }
                TrainingState.PRODUCING -> {
                    difficultyPicker.hide()
                    button.hide()
                    header.hide()
                }
                TrainingState.RECEIVING -> {
                    difficultyPicker.hide()
                    button.hide()
                    header.show()
                    header.text = getString(R.string.memory_tooltip_sequence)
                }
                TrainingState.ERROR -> {
                    difficultyPicker.show()
                    button.show()
                    button.text = getString(R.string.memory_button_retry)
                    header.show()
                    header.text = getString(R.string.memory_tooltip_error)
                }
                TrainingState.END -> {
                    difficultyPicker.hide()
                    button.hide()
                    header.show()
                    header.text = getString(R.string.memory_tooltip_success)
                }
            }
        }
        rv_points.adapter = adapter
        observeEvent(viewModel.errorLiveData) {
            toast(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        rv_points.adapter = null
    }
}
