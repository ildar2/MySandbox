/**
 * (C) Copyright 2021 Ildar Ishalin
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
package kz.ildar.sandbox.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber
import java.io.Serializable

/**
 * Класс для работы со списками с функционалом [DiffUtil] для анимаций и производительности
 * Может работать с несколькими видами элементов
 * В качестве viewType выступает лэйаут элемента [DisplayDiffItem.layout]
 * поэтому, в пределах одного списка все лэйауты должны быть уникальными
 *
 * Для правильной работы списка в [DisplayDiffItem] хранится
 * [DisplayDiffItem.itemDisplayId] - идентивикатор поля
 * [DisplayDiffItem.itemDisplayValue] - показывает что поле изменилось или нет
 *
 * Для работы нужно обозначить DTO с их лэйаутами и переопределить [createViewHolder]
 * Все элементы должны имплементить интерфейс [DisplayDiffItem]
 *
 * см. подклассы для примера
 */
abstract class DisplayDiffAdapter : RecyclerView.Adapter<DisplayViewHolder<DisplayDiffItem>>() {

    private val diffCallback = object : DiffUtil.ItemCallback<DisplayDiffItem>() {
        override fun areItemsTheSame(
            oldItem: DisplayDiffItem, newItem: DisplayDiffItem
        ): Boolean {
            return oldItem.itemDisplayId == newItem.itemDisplayId
        }

        override fun areContentsTheSame(
            oldItem: DisplayDiffItem, newItem: DisplayDiffItem
        ): Boolean {
//            Timber.w("areContentsTheSame for ${oldItem.itemDisplayId}: ${oldItem.itemDisplayValue} ${newItem.itemDisplayValue}")
            if (newItem.itemDisplayValue == DIFF_FORCE_UPDATE) return false
            return oldItem.itemDisplayValue == newItem.itemDisplayValue
        }

        override fun getChangePayload(oldItem: DisplayDiffItem, newItem: DisplayDiffItem): Any? {
            return if (false/*newItem is CommonInputDisplay && !newItem.shouldUpdate*/) {
                DIFF_SKIP_UPDATE
            } else
                super.getChangePayload(oldItem, newItem)
        }
    }
    private val differ = AsyncListDiffer(this, diffCallback)
    var items: List<DisplayDiffItem>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    fun removeItem(item: DisplayDiffItem) {
        if (differ.currentList.contains(item)) {
            differ.currentList.remove(item)
        }
    }

    override fun getItemViewType(position: Int) = items[position].layout

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DisplayViewHolder<DisplayDiffItem> = createViewHolder(
        LayoutInflater.from(parent.context).inflate(
            viewType,
            parent,
            false
        ), viewType
    ) as DisplayViewHolder<DisplayDiffItem>

    abstract fun createViewHolder(view: View, viewType: Int): DisplayViewHolder<*>

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(
        holder: DisplayViewHolder<DisplayDiffItem>,
        position: Int
    ) = holder.bind(differ.currentList[position])

    override fun onBindViewHolder(
        holder: DisplayViewHolder<DisplayDiffItem>,
        position: Int,
        payloads: MutableList<Any>
    ) = holder.bind(differ.currentList[position], payloads)

    override fun onViewRecycled(holder: DisplayViewHolder<DisplayDiffItem>) = holder.unbind()
}

const val DIFF_FORCE_UPDATE = "display.diff.adapter.force.update"
const val DIFF_SKIP_UPDATE = "display.diff.adapter.skip.update"

abstract class DisplayDiffItem(
    layout: Int,
    open val itemDisplayId: String,
) : DisplayItem(layout), Serializable {
    abstract var itemDisplayValue: String
}