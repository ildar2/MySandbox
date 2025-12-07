package kz.ildar.sandbox.ui.main.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import kz.ildar.sandbox.R
import kz.ildar.sandbox.databinding.FragmentColorListBinding
import kz.ildar.sandbox.ui.main.color.ColorFragment
import kz.ildar.sandbox.utils.EventObserver
import org.koin.androidx.viewmodel.ext.android.viewModel

class ColorListFragment : Fragment(R.layout.fragment_color_list) {

    private val viewModel: ColorListViewModel by viewModel()
    private val adapter = ColorListAdapter()
    private lateinit var binding: FragmentColorListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentColorListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.include.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        binding.include.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        binding.include.toolbar.title = getString(R.string.color_list_title)

        initList()
        initViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerView.adapter = null
    }

    private fun initList() {
        binding.recyclerView.adapter = adapter
    }

    private fun initViewModel() {
        viewModel.listLiveData.observe(viewLifecycleOwner, Observer {
            adapter.items = it
        })
        viewModel.navigation.observe(viewLifecycleOwner, EventObserver {
            Navigation.findNavController(binding.recyclerView).navigate(
                R.id.open_colorFragment,
                bundleOf(ColorFragment.EXTRA_COLOR to it)
            )
        })
    }
}