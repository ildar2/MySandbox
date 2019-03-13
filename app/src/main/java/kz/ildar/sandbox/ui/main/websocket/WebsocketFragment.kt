package kz.ildar.sandbox.ui.main.websocket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_websocket.*
import kz.ildar.sandbox.R
import kz.ildar.sandbox.ui.BaseViewModel
import kz.ildar.sandbox.utils.EventObserver
import org.koin.android.viewmodel.ext.android.getViewModel
import timber.log.Timber

class WebsocketFragment : Fragment() {
    private lateinit var viewModel: WebsocketViewModel

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
        viewModel.errorLiveData.observe(this, EventObserver { error ->
            Timber.w("errorLiveData fired")
            activity?.run {
                val text = error.format(this)
                Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.logLiveData.observe(this, Observer { logEntry ->
            Timber.w("logLiveData fired")
            logView.append(logEntry)
            logView.append("\n")
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_websocket, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.title = "Websocket echo"
        viewModel.start()
    }
}