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
package kz.ildar.sandbox.ui.main.hello

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.Fragment
import kz.ildar.sandbox.R
import kz.ildar.sandbox.databinding.FragmentHelloBinding
import kz.ildar.sandbox.ui.Status
import kz.ildar.sandbox.utils.ext.observe
import kz.ildar.sandbox.utils.ext.observeEvent
import kz.ildar.sandbox.utils.ext.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class HelloFragment : Fragment(R.layout.fragment_hello) {

    private val viewModel: HelloViewModel by viewModel()
    private lateinit var binding: FragmentHelloBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHelloBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun initViewModel() {
        observe(viewModel.statusLiveData) { status ->
            when (status) {
                Status.SHOW_LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                Status.HIDE_LOADING -> {
                    binding.progressBar.visibility = View.GONE
                }
                else -> Unit
            }
        }
        observeEvent(viewModel.errorLiveData) { error ->
            toast(error)
        }
        observeEvent(viewModel.greetingLiveData) { message ->
            toast(message)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViewModel()
        binding.include.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        binding.include.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        binding.include.toolbar.title = getString(R.string.hello_fragment_title)
        binding.makeRequest.setOnClickListener {
            loadGreetings()
        }

        binding.nameInput.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                return if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loadGreetings()
                    true
                } else false
            }
        })
    }

    fun loadGreetings() {
        viewModel.loadGreetings(binding.nameInput.text.toString())
    }
}