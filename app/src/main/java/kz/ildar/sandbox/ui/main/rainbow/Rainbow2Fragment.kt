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
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_rainbow_2.*
import kotlinx.android.synthetic.main.include_toolbar.*
import kz.ildar.sandbox.R
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

    private fun initViewModel() {
        observe(viewModel.rainbowItemLiveData) { item ->
            tv_result.text = "Совпадает ли название цвета наверху с цветом текста внизу?"
            tv_result.textColorRes(R.color.gray_solid)
            rainbowView.text = item.text1
            rainbowView.setTextColor(item.textColor1)
            rainbow2View.text = item.text2
            rainbow2View.setTextColor(item.textColor2)
        }
        observe(viewModel.statusLiveData) {
            when (it) {
                Status.SHOW_LOADING -> {
                    tv_result.show()
                    startView.hide()
                    minutePickerLabel.hide()
                    minutePicker.hide()
                    rainbowView.show()
                    rainbow2View.show()
                    b_yes.show()
                    b_no.show()
                }
                Status.HIDE_LOADING -> {
                    tv_result.hide()
                    startView.show()
                    minutePickerLabel.show()
                    minutePicker.show()
                    rainbowView.hide()
                    rainbow2View.hide()
                    b_yes.hide()
                    b_no.hide()
                }
                Status.SUCCESS -> {
                    tv_result.show()
                    tv_result.text = "Правильно!"
                    tv_result.textColorRes(R.color.success)
                }
                else -> {
                    tv_result.show()
                    tv_result.text = "Неправильно!"
                    tv_result.textColorRes(R.color.error)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViewModel()
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        toolbar.hide()

        minutePicker.minValue = 1
        minutePicker.maxValue = 10

        startView.setOnClickListener {
            viewModel.start(terminate = minutePicker.value * 60000L)
        }
        contentView.setOnClickListener {
            viewModel.stop()
        }
        b_yes.setSafeOnClickListener {
            viewModel.answerYes()
        }
        b_no.setSafeOnClickListener {
            viewModel.answerNo()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.stop()
    }
}