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
package kz.ildar.sandbox.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.include_toolbar.*
import kz.ildar.sandbox.R
import org.koin.android.viewmodel.ext.android.getViewModel
import timber.log.Timber

class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel()
        viewModel.parentLiveData.observe(this, Observer { value ->
            Timber.w("parentLiveData fired")
            toolbar.title = value//not working
            (activity as? AppCompatActivity)?.supportActionBar?.title = value//working
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_main, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)

        viewModel.getData()

        childView.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.open_childFragment))

        rainbowView.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.open_rainbowFragment))

        helloView.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.open_helloFragment))

        websocketView.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.open_websocketFragment))

        motionView.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.open_motionFragment))

        multiCallView.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.open_multiCallFragment))

        colorView.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.open_colorFragment))

        colorListView.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.open_colorListFragment))
    }

    override fun onPause() {
        super.onPause()
        curtain.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        curtain.visibility = View.GONE
    }
}