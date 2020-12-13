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
package kz.ildar.sandbox.ui.main

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.android.synthetic.main.item_main_button.*
import kz.ildar.sandbox.R
import kz.ildar.sandbox.utils.DisplayItem
import kz.ildar.sandbox.utils.DisplayViewHolder
import kz.ildar.sandbox.utils.EventWrapper
import kz.ildar.sandbox.utils.ext.post
import kz.ildar.sandbox.utils.wrapEvent

class MainViewModel : ViewModel() {
    val actionListLiveData: LiveData<List<MainButtonDisplay>> = MutableLiveData(
        listOf(
            MainButtonDisplay("Proceed to Stories") { action(MainNavAction.OPEN_STORIES) },
            MainButtonDisplay("Proceed to Playground") { action(MainNavAction.OPEN_PLAYGROUND) },
            MainButtonDisplay("Proceed to Child") { action(MainNavAction.OPEN_CHILD) },
            MainButtonDisplay("Proceed to Rainbow") { action(MainNavAction.OPEN_RAINBOW) },
            MainButtonDisplay("Proceed to Hello requests") { action(MainNavAction.OPEN_HELLO) },
            MainButtonDisplay("Proceed to Websocket") { action(MainNavAction.OPEN_WEBSOCKET) },
            MainButtonDisplay("Proceed to Proximity sensor") { action(MainNavAction.OPEN_MOTION) },
            MainButtonDisplay("Proceed to Multicall") { action(MainNavAction.OPEN_MULTICALL) },
            MainButtonDisplay("Proceed to Color picker") { action(MainNavAction.OPEN_COLOR) },
            MainButtonDisplay("Proceed to Color list") { action(MainNavAction.OPEN_COLOR_LIST) }
        )
    )
    val actionEventLiveData = MutableLiveData<EventWrapper<MainNavAction>>()

    private fun action(action: MainNavAction) {
        actionEventLiveData post action.wrapEvent()
    }
}

class MainButtonDisplay(
    val text: String,
    val click: () -> Unit
) : DisplayItem(R.layout.item_main_button) {
    class ViewHolder(view: View) : DisplayViewHolder<MainButtonDisplay>(view) {
        override fun bind(item: MainButtonDisplay) {
            tv_button_text.text = item.text
            tv_button_text.setOnClickListener {
                item.click()
            }
        }
    }
}

enum class MainNavAction {
    OPEN_STORIES,
    OPEN_CHILD,
    OPEN_PLAYGROUND,
    OPEN_RAINBOW,
    OPEN_HELLO,
    OPEN_WEBSOCKET,
    OPEN_MOTION,
    OPEN_MULTICALL,
    OPEN_COLOR,
    OPEN_COLOR_LIST
}
