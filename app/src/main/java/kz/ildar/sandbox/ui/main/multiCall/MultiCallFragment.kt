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
package kz.ildar.sandbox.ui.main.multiCall

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_multi_call.*
import kotlinx.android.synthetic.main.include_toolbar.*
import kz.ildar.sandbox.R
import kz.ildar.sandbox.ui.Status
import kz.ildar.sandbox.utils.ext.observe
import kz.ildar.sandbox.utils.ext.observeEvent
import kz.ildar.sandbox.utils.ext.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class MultiCallFragment : Fragment(R.layout.fragment_multi_call) {

    private val viewModel: MultiCallViewModel by viewModel()

    private fun initViewModel() {
        observe(viewModel.statusLiveData) { status ->
            when (status) {
                Status.SHOW_LOADING -> {
                    progressBar.visibility = View.VISIBLE
                }
                Status.HIDE_LOADING -> {
                    progressBar.visibility = View.GONE
                }
                else -> Unit
            }
        }
        observeEvent(viewModel.errorLiveData) { error ->
            toast(error)
        }
        observeEvent(viewModel.logLiveData) { message ->
            activity?.run {
                val text = message.format(this)
                logView.append(text)
                logView.append("\n")
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViewModel()
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        toolbar.title = getString(R.string.multicall_title)

        multipleCallView.setOnClickListener {
            logView.text = ""
            viewModel.multiCall()
        }

        twoCallView.setOnClickListener {
            logView.text = ""
            viewModel.twoCall()
        }

        threeCallView.setOnClickListener {
            logView.text = ""
            viewModel.threeCall()
        }

        arrayCallView.setOnClickListener {
            logView.text = ""
            viewModel.arrayCall()
        }
    }
}