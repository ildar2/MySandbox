/**
 * (C) Copyright 2019 Ildar Ishalin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package kz.ildar.sandbox.ui.main.child

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kz.ildar.sandbox.data.HelloRepository
import kz.ildar.sandbox.data.model.Child
import kz.ildar.sandbox.utils.EventWrapper

class ChildViewModel(private val helloRepository: HelloRepository) : ViewModel() {
    private val _openFragmentEvents = MutableLiveData<EventWrapper<View>>()
    val openFragmentEvents: LiveData<EventWrapper<View>>
        get() = _openFragmentEvents

    val childLiveData = MutableLiveData<String>()

    fun getTitle() {
        childLiveData.value = Child().sayMyName()
    }

    fun getUrl(): String {
        return helloRepository.getImageUrl()
    }

    fun userClicked(view: View) {
        _openFragmentEvents.value = EventWrapper(view)
    }
}