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
            MainButtonDisplay("Stories") { action(MainNavAction.OPEN_STORIES) },
            MainButtonDisplay("Playground") { action(MainNavAction.OPEN_PLAYGROUND) },
            MainButtonDisplay("Child") { action(MainNavAction.OPEN_CHILD) },
            MainButtonDisplay("Rainbow") { action(MainNavAction.OPEN_RAINBOW) },
            MainButtonDisplay("Rainbow 2") { action(MainNavAction.OPEN_RAINBOW2) },
            MainButtonDisplay("Hello requests") { action(MainNavAction.OPEN_HELLO) },
            MainButtonDisplay("Websocket") { action(MainNavAction.OPEN_WEBSOCKET) },
            MainButtonDisplay("Proximity sensor") { action(MainNavAction.OPEN_MOTION) },
            MainButtonDisplay("Multicall") { action(MainNavAction.OPEN_MULTICALL) },
            MainButtonDisplay("Color picker") { action(MainNavAction.OPEN_COLOR) },
            MainButtonDisplay("Color list") { action(MainNavAction.OPEN_COLOR_LIST) },
            MainButtonDisplay("Touch panel") { action(MainNavAction.OPEN_TOUCH_PANEL) }
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
    OPEN_RAINBOW2,
    OPEN_HELLO,
    OPEN_WEBSOCKET,
    OPEN_MOTION,
    OPEN_MULTICALL,
    OPEN_COLOR,
    OPEN_COLOR_LIST,
    OPEN_TOUCH_PANEL,
}
