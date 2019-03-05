package kz.ildar.sandbox.ui.main.child

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import kz.ildar.sandbox.data.HelloRepository
import kz.ildar.sandbox.data.model.Child

class ChildViewModel(val repo: HelloRepository) : ViewModel() {
    val childLiveData = MutableLiveData<String>()

    fun getData() {
        val c = Child()
        childLiveData.value = c.sayMyName()
    }

    fun getUrl(): String {
        return repo.getImageUrl()
    }
}