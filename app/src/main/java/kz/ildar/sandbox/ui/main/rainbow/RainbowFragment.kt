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
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_rainbow.*
import kotlinx.android.synthetic.main.include_toolbar.view.*
import kz.ildar.sandbox.R
import kz.ildar.sandbox.ui.Status
import kz.ildar.sandbox.utils.ext.hide
import kz.ildar.sandbox.utils.ext.show
import kz.ildar.sandbox.utils.ext.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class RainbowFragment : Fragment(R.layout.fragment_rainbow) {

    private val viewModel: RainbowViewModel by viewModel()

    private fun initViewModel() {
        viewModel.rainbowItemLiveData.observe(viewLifecycleOwner, Observer { item ->
            val params = rainbowView.layoutParams as ConstraintLayout.LayoutParams
            params.horizontalBias = item.x
            params.verticalBias = item.y
            rainbowView.layoutParams = params
            rainbowView.text = item.text
            rainbowView.setTextColor(item.textColor)
            rainbowView.setBackgroundColor(item.bgColor)
        })
        viewModel.statusLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                Status.SHOW_LOADING -> {
                    tooltip.hide()
                    startView.hide()
                    minutePickerLabel.hide()
                    minutePicker.hide()
                    rainbowView.show()
                }
                Status.HIDE_LOADING -> {
                    tooltip.show()
                    startView.show()
                    minutePickerLabel.show()
                    minutePicker.show()
                    rainbowView.hide()
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
        toolbar.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        toolbar.toolbar.hide()

        minutePicker.minValue = 1
        minutePicker.maxValue = 10

        startView.setOnClickListener {
            viewModel.start(terminate = minutePicker.value * 60000L)
        }
        contentView.setOnClickListener {
            viewModel.stop()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.stop()
    }
}