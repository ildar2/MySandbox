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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_rainbow.*
import kotlinx.android.synthetic.main.include_toolbar.*
import kz.ildar.sandbox.R
import kz.ildar.sandbox.ui.Status
import org.koin.android.viewmodel.ext.android.getViewModel

class RainbowFragment : Fragment() {
    private lateinit var viewModel: RainbowViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel()

        viewModel.rainbowItemLiveData.observe(this, Observer { item ->
            val params = rainbowView.layoutParams as ConstraintLayout.LayoutParams
            params.horizontalBias = item.x
            params.verticalBias = item.y
            rainbowView.layoutParams = params
            rainbowView.text = item.text
            rainbowView.setTextColor(item.textColor)
            rainbowView.setBackgroundColor(item.bgColor)
        })
        viewModel.statusLiveData.observe(this, Observer {
            when (it) {
                Status.SHOW_LOADING -> {
                    startView.visibility = View.GONE
                    minutePickerLabel.visibility = View.GONE
                    minutePicker.visibility = View.GONE
                    rainbowView.visibility = View.VISIBLE
                }
                Status.HIDE_LOADING -> {
                    startView.visibility = View.VISIBLE
                    minutePickerLabel.visibility = View.VISIBLE
                    minutePicker.visibility = View.VISIBLE
                    rainbowView.visibility = View.GONE
                }
                Status.SUCCESS -> {
                    Toast.makeText(activity, "Игра закончилась!", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    //do nothing
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_rainbow, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.visibility = View.GONE

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