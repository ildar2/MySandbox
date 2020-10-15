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

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.include_toolbar.*
import kz.ildar.sandbox.R
import kz.ildar.sandbox.ui.main.stories.StoriesActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment(R.layout.fragment_main) {

    private val viewModel: MainViewModel by viewModel()

    private fun initViewModel() {
        viewModel.parentLiveData.observe(viewLifecycleOwner, Observer { value ->
            toolbar.title = value//not working
            (activity as? AppCompatActivity)?.supportActionBar?.title = value//working
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViewModel()
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)

        viewModel.getData()

        storyView.setOnClickListener {
            startActivity(Intent(context, StoriesActivity::class.java))
        }
        playgroundView.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.open_playgroundFragment))

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
        //we can hide screen from task manager
//        curtain.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        curtain.visibility = View.GONE
    }
}