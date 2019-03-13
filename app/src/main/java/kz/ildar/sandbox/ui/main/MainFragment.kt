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

        parentView.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.open_childFragment))

        helloView.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.open_helloFragment))

        websocketView.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.open_websocketFragment))

        motionView.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.open_motionFragment))
    }
}