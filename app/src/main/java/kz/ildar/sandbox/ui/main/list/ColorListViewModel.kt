package kz.ildar.sandbox.ui.main.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kz.ildar.sandbox.data.ColorRepository
import kz.ildar.sandbox.ui.main.color.ColorMutable

class ColorListViewModel(private val colorRepository: ColorRepository) : ViewModel() {
    val listLiveData = MutableLiveData<List<ColorMutable>>()

    fun getData() {
        listLiveData.value = colorRepository.getColorList().map { ColorMutable.from(it) }
    }
}