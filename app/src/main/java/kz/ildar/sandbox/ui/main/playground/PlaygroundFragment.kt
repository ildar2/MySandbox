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
package kz.ildar.sandbox.ui.main.playground

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kz.ildar.sandbox.R
import kz.ildar.sandbox.databinding.FragmentPlaygroundBinding
import kz.ildar.sandbox.utils.ext.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaygroundFragment : Fragment(R.layout.fragment_playground) {

    private val viewModel: PlaygroundViewModel by viewModel()

    private var switch = true
    private lateinit var binding: FragmentPlaygroundBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaygroundBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.include.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        binding.include.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        binding.button.setOnClickListener {
            if (switch) {
                binding.et.inputType =
                    InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_NUMBER_FLAG_SIGNED
            } else {
                binding.et.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
            }
            toast("inputType is ${if (switch) "number" else "text"}")
            switch = !switch
        }
        binding.buttonVibrate.setOnClickListener {
            viewModel.toggleVibrating()
        }
        viewModel.vibratingLiveData.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                binding.buttonVibrate.text = "Stop vibrating"
            } else {
                binding.buttonVibrate.text = "Vibrate"
            }
        })
    }
}