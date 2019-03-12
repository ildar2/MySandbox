package kz.ildar.sandbox.ui.main

import androidx.lifecycle.MutableLiveData
import kz.ildar.sandbox.data.model.Parent
import kz.ildar.sandbox.ui.BaseViewModel

class MainViewModel : BaseViewModel() {
    val parentLiveData = MutableLiveData<String>()

    fun getData() {
        parentLiveData.value = Parent().sayMyName()
    }
}