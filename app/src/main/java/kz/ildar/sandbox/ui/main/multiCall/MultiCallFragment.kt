package kz.ildar.sandbox.ui.main.multiCall

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
import kotlinx.android.synthetic.main.fragment_multi_call.*
import kz.ildar.sandbox.R
import kz.ildar.sandbox.ui.BaseViewModel
import kz.ildar.sandbox.utils.EventObserver
import org.koin.android.viewmodel.ext.android.getViewModel
import timber.log.Timber

class MultiCallFragment : Fragment() {

    private lateinit var viewModel: MultiCallViewModel

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
        viewModel.logLiveData.observe(this, EventObserver { message ->
            activity?.run {
                val text = message.format(this)
                logView.append(text)
                logView.append("\n")
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.fragment_multi_call, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.title = "Multi-request"

        multipleCallView.setOnClickListener {
            logView.text = ""
            viewModel.multiCall()
        }

        twoCallView.setOnClickListener {
            logView.text = ""
            viewModel.twoCall()
        }

        threeCallView.setOnClickListener {
            logView.text = ""
            viewModel.threeCall()
        }

        arrayCallView.setOnClickListener {
            logView.text = ""
            viewModel.arrayCall()
        }
    }
}