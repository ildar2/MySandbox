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
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kz.ildar.sandbox.ui.main.list.ColorListAdapter

/**
 * Класс для работы со списками
 * Может работать с несколькими видами элементов
 * В качестве viewType выступает лэйаут элемента [DisplayItem.layout]
 * поэтому, в пределах одного списка все лэйауты должны быть уникальными
 *
 * Для работы нужно обозначить DTO с их лэйаутами и переопределить [createViewHolder]
 *
 * см. [ColorListAdapter] для примера
 */
abstract class DisplayAdapter(
    items: List<DisplayItem> = emptyList()
) : RecyclerView.Adapter<DisplayViewHolder<DisplayItem>>() {

    var items = items
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemViewType(position: Int) = items[position].layout

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DisplayViewHolder<DisplayItem> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return createViewHolder(view, viewType) as DisplayViewHolder<DisplayItem>
    }

    /**
     * Erased type for brevity, item should always implement [DisplayItem]
     */
    abstract fun createViewHolder(view: View, @LayoutRes viewType: Int): DisplayViewHolder<*>

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: DisplayViewHolder<DisplayItem>, position: Int) =
        holder.bind(items[position])

    override fun onViewRecycled(holder: DisplayViewHolder<DisplayItem>) = holder.unbind()
}

/**
 * Общий интерфейс для элементов отображения в списке адаптера
 */
abstract class DisplayItem(val layout: Int)

/**
 * Общий интерфейс для вьюхолдеров
 * Связывает элементы [DisplayItem] и их вьюшки [DisplayItem.layout]
 * Обычно лучше делать inner-классом элемента для понятности, но не обязательно
 */
abstract class DisplayViewHolder<E>(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    abstract fun bind(item: E)
    open fun bind(item: E, payloads: MutableList<Any>) = bind(item)
    open fun unbind() = Unit
}
