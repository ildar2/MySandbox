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
package kz.ildar.sandbox.ui.main.child

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigator
import com.bumptech.glide.Glide
import kz.ildar.sandbox.R
import kz.ildar.sandbox.databinding.FragmentChildBinding
import kz.ildar.sandbox.utils.EventObserver
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChildFragment : Fragment(R.layout.fragment_child) {
    companion object {
        private const val ARG_COUNTER = "counter"
    }

    private val viewModel: ChildViewModel by viewModel()
    private lateinit var binding: FragmentChildBinding
    private val counter: Int
        get() = arguments?.getInt(ARG_COUNTER) ?: 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChildBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.include.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        binding.include.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        initViewModel()
        viewModel.getTitle()

        binding.childView.text = getString(R.string.child_fragment_label, counter)
        binding.childView.setOnClickListener {
            viewModel.userClicked(it)
        }

        Glide.with(this)
            .load(viewModel.getUrl())
            .into(binding.image)
    }

    private fun initViewModel() {
        viewModel.childLiveData.observe(viewLifecycleOwner, Observer { value ->
//            toolbar.title = value
            binding.childView.text = value
        })
        viewModel.capabilities.observe(viewLifecycleOwner) { value ->
            binding.childView.append("\n")
            binding.childView.append(value)
        }
        viewModel.openFragmentEvents.observe(viewLifecycleOwner, EventObserver { view ->
            val extras = FragmentNavigator.Extras.Builder().build()
            Navigation.findNavController(view).navigate(
                R.id.action_recursive_child,
                bundleOf(ARG_COUNTER to counter + 1),
                null,
                extras
            )
        })
    }
}