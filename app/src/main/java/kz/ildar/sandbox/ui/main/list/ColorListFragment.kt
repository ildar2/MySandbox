package kz.ildar.sandbox.ui.main.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_color_list.*
import kotlinx.android.synthetic.main.include_toolbar.*
import kz.ildar.sandbox.R
import kz.ildar.sandbox.ui.main.color.ColorFragment
import kz.ildar.sandbox.utils.EventObserver
import org.koin.androidx.viewmodel.ext.android.getViewModel

class ColorListFragment : Fragment() {

    private lateinit var viewModel: ColorListViewModel
    private val adapter = ColorListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_color_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        toolbar.title = getString(R.string.color_list_title)

        initList()
        initViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerView.adapter = null
    }

    private fun initList() {
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.adapter = adapter
    }

    private fun initViewModel() {
        viewModel = getViewModel()
        viewModel.listLiveData.observe(viewLifecycleOwner, Observer {
            adapter.items = it
        })
        viewModel.navigation.observe(viewLifecycleOwner, EventObserver {
            Navigation.findNavController(recyclerView).navigate(
                R.id.open_colorFragment,
                Bundle().apply {
                    putParcelable(ColorFragment.EXTRA_COLOR, it)
                }
            )
        })
    }
}