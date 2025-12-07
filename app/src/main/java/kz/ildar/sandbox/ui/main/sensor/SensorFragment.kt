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
package kz.ildar.sandbox.ui.main.sensor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.afollestad.materialdialogs.MaterialDialog
import kz.ildar.sandbox.R
import kz.ildar.sandbox.databinding.FragmentMotionBinding
import kz.ildar.sandbox.utils.EventObserver
import org.koin.androidx.viewmodel.ext.android.viewModel

class SensorFragment : Fragment(R.layout.fragment_motion) {
    private val viewModel: SensorViewModel by viewModel()
    private lateinit var binding: FragmentMotionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMotionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.include.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        binding.include.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        binding.include.toolbar.title = getString(R.string.proximity_title)

        viewModel.sensorLiveData.observe(viewLifecycleOwner, EventObserver {
            dialog()
        })
        SensorCallbacks.mode.observe(viewLifecycleOwner, Observer {
            binding.text.text = when (it) {
                SensorMode.OPEN -> "Open mode"
                else -> "Secret mode"
            }
        })
    }

    private fun dialog() {
        SensorCallbacks.enabled = false
        MaterialDialog(requireActivity()).show {
            message(text = if (SensorCallbacks.mode.value == SensorMode.OPEN) "Enable secret mode?"
            else "Disable secret mode?")
            positiveButton(text = "Yes") {
                SensorCallbacks.toggle()
                SensorCallbacks.enabled = true
            }
            negativeButton(text = "No") {
                SensorCallbacks.enabled = true
            }
        }
    }
}