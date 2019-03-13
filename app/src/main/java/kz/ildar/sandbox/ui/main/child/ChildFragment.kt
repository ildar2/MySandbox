package kz.ildar.sandbox.ui.main.child

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigator
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.child_fragment.*
import kz.ildar.sandbox.R
import kz.ildar.sandbox.utils.EventObserver
import org.koin.android.viewmodel.ext.android.getViewModel

class ChildFragment : Fragment() {

    private lateinit var viewModel: ChildViewModel
    private var counter: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        counter = arguments?.getInt("counter") ?: 0
        viewModel = getViewModel()

        viewModel.childLiveData.observe(this, Observer { value ->
            toolbar.title = value
        })
        viewModel.openFragmentEvents.observe(this, EventObserver { view ->
            val extras = FragmentNavigator.Extras.Builder()
                .addSharedElement(toolbar, "toolbar")//todo
                .build()
            Navigation.findNavController(view).navigate(
                R.id.action_recursive_child,
                Bundle().apply { putInt("counter", counter + 1) },
                null,
                extras
            )
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.child_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.getTitle()

        childView.text = getString(R.string.child_fragment_label, counter)
        childView.setOnClickListener {
            viewModel.userClicked(it)
        }

        Glide.with(this)
            .load(viewModel.getUrl())
            .into(image)
    }
}