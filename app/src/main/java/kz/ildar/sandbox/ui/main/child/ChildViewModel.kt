package kz.ildar.sandbox.ui.main.child

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kz.ildar.sandbox.data.HelloRepository
import kz.ildar.sandbox.data.model.Child
import kz.ildar.sandbox.utils.Event

class ChildViewModel(val repo: HelloRepository) : ViewModel() {
    private val _openFragmentEvents = MutableLiveData<Event<View>>()
    val openFragmentEvents: LiveData<Event<View>>
        get() = _openFragmentEvents

    val childLiveData = MutableLiveData<String>()

    fun getTitle() {
        childLiveData.value = Child().sayMyName()
    }

    fun getUrl(): String {
        return repo.getImageUrl()
    }

    fun userClicked(view: View) {
        _openFragmentEvents.value = Event(view)
    }
}