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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import kz.ildar.sandbox.R
import kz.ildar.sandbox.databinding.FragmentMainBinding
import kz.ildar.sandbox.ui.main.stories.StoriesActivity
import kz.ildar.sandbox.utils.DisplayAdapter
import kz.ildar.sandbox.utils.ext.observe
import kz.ildar.sandbox.utils.ext.observeEvent
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment(R.layout.fragment_main) {

    private val viewModel: MainViewModel by viewModel()
    private lateinit var binding: FragmentMainBinding
    private val adapter = object : DisplayAdapter() {
        override fun createViewHolder(view: View, viewType: Int) = MainButtonDisplay.ViewHolder(view)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun initViewModel() {
        observe(viewModel.actionListLiveData) {
            adapter.items = it
        }
        observeEvent(viewModel.actionEventLiveData) {
            when (it) {
                MainNavAction.OPEN_STORIES -> startActivity(Intent(context, StoriesActivity::class.java))
                MainNavAction.OPEN_CHILD -> Navigation.findNavController(requireView()).navigate(R.id.open_childFragment)
                MainNavAction.OPEN_MEMORY_TRAINING -> Navigation.findNavController(requireView()).navigate(R.id.memoryTrainingFragment)
                MainNavAction.OPEN_PLAYGROUND -> Navigation.findNavController(requireView()).navigate(R.id.open_playgroundFragment)
                MainNavAction.OPEN_RAINBOW -> Navigation.findNavController(requireView()).navigate(R.id.open_rainbowFragment)
                MainNavAction.OPEN_RAINBOW2 -> Navigation.findNavController(requireView()).navigate(R.id.rainbow2Fragment)
                MainNavAction.OPEN_HELLO -> Navigation.findNavController(requireView()).navigate(R.id.open_helloFragment)
                MainNavAction.OPEN_WEBSOCKET -> Navigation.findNavController(requireView()).navigate(R.id.open_websocketFragment)
                MainNavAction.OPEN_MOTION -> Navigation.findNavController(requireView()).navigate(R.id.open_motionFragment)
                MainNavAction.OPEN_MULTICALL -> Navigation.findNavController(requireView()).navigate(R.id.open_multiCallFragment)
                MainNavAction.OPEN_COLOR -> Navigation.findNavController(requireView()).navigate(R.id.open_colorFragment)
                MainNavAction.OPEN_COLOR_LIST -> Navigation.findNavController(requireView()).navigate(R.id.open_colorListFragment)
                MainNavAction.OPEN_TOUCH_PANEL -> Navigation.findNavController(requireView()).navigate(R.id.touchPanelFragment)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViewModel()
        (activity as? AppCompatActivity)?.setSupportActionBar(binding.include.toolbar)
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Main menu"

        binding.rvButtons.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvButtons.adapter = null
    }

    override fun onPause() {
        super.onPause()
        //we can hide screen from task manager
//        curtain.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        binding.curtain.visibility = View.GONE
    }
}