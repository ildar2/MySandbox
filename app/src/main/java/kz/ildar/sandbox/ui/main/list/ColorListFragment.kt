package kz.ildar.sandbox.ui.main.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_color_list.*
import kotlinx.android.synthetic.main.include_toolbar.*
import kz.ildar.sandbox.R
import kz.ildar.sandbox.ui.main.color.ColorFragment
import kz.ildar.sandbox.utils.EventObserver
import org.koin.android.viewmodel.ext.android.getViewModel

class ColorListFragment : Fragment() {

    private lateinit var viewModel: ColorListViewModel
    private val adapter = ColorListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_color_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar?.title = "Color list"

        initList()
        initViewModel()
    }

    private fun initList() {
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.adapter = adapter
    }

    private fun initViewModel() {
        viewModel = getViewModel()
        viewModel.listLiveData.observe(viewLifecycleOwner, Observer {
            adapter.items = it.toMutableList()
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