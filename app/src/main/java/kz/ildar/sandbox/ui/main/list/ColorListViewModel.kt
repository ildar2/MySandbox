package kz.ildar.sandbox.ui.main.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import kz.ildar.sandbox.data.ColorRepository
import kz.ildar.sandbox.data.model.ColorModel

class ColorListViewModel(private val colorRepository: ColorRepository) : ViewModel() {
    val listLiveData = MutableLiveData<List<ColorModel>>()

    fun getData() {
        listLiveData.value = colorRepository.getColorList()
    }
}
