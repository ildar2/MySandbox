package kz.ildar.sandbox.ui.main

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.main_fragment.*
import kz.ildar.sandbox.R
import kz.ildar.sandbox.ui.BaseViewModel
import org.koin.android.viewmodel.ext.android.getViewModel
import timber.log.Timber

class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel()
        viewModel.statusLiveData.observe(this, Observer { status ->
            when (status) {
                BaseViewModel.Status.SHOW_LOADING -> {
                    progressBar.visibility = View.VISIBLE
                }
                BaseViewModel.Status.HIDE_LOADING -> {
                    progressBar.visibility = View.GONE
                }
            }
        })
        viewModel.errorLiveData.observe(this, Observer { error ->
            Timber.w("errorLiveData fired")
            activity?.run {
                val text = error.format(this)
                Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.parentLiveData.observe(this, Observer { value ->
            Timber.w("parentLiveData fired")
            toolbar.title = value//not working
            (activity as? AppCompatActivity)?.supportActionBar?.title = value//working
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.main_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)

        viewModel.getData()

        parentView.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.open_childFragment))

        helloView.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.open_helloFragment))

        websocketView.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.open_websocketFragment))
    }
}