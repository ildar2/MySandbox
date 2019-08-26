package kz.ildar.sandbox.ui.main.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kz.ildar.sandbox.data.ColorRepository
import kz.ildar.sandbox.ui.main.color.ColorMutable
import kz.ildar.sandbox.utils.EventWrapper

class ColorListViewModel(
    private val colorRepository: ColorRepository
) : ViewModel() {

    private val _listLiveData = MutableLiveData<List<ColorDisplay>>()
    val listLiveData: LiveData<List<ColorDisplay>>
        get() = _listLiveData

    private val _navigation = MutableLiveData<EventWrapper<ColorMutable>>()
    val navigation: LiveData<EventWrapper<ColorMutable>>
        get() = _navigation

    init {
        getData()
    }

    private fun getData() {
        _listLiveData.value = colorRepository.getColorList().map { model ->
            val colorMutable = ColorMutable.from(model)
            ColorDisplay(
                colorMutable.name,
                colorMutable.getHexString(),
                colorMutable.getColor()
            ) {
                _navigation.value = EventWrapper(colorMutable)
            }
        }
    }
}