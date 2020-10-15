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
package kz.ildar.sandbox.ui.main.stories

import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.LayoutRes
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_story_full.*
import kz.ildar.sandbox.R
import kz.ildar.sandbox.utils.DisplayAdapter
import kz.ildar.sandbox.utils.DisplayItem
import kz.ildar.sandbox.utils.DisplayViewHolder

class StoriesAdapter : DisplayAdapter() {

    var stories = listOf<StoryFullDisplay>()
        set(value) {
            field = value
            items = value
        }

    override fun createViewHolder(view: View, @LayoutRes viewType: Int) = when (viewType) {
        R.layout.item_story_full -> StoryFullDisplay.ViewHolder(view)
        else -> throw RuntimeException("Unknown type $viewType, you should modify createViewHolder")
    }
}

/**
 * Отображение набора страниц одной стори
 * отображается одна из страниц [pages] с индексом [pagePosition]
 */
class StoryFullDisplay(
    val pageCount: Int,
    @ColorInt
    val pages: List<Int>,
    var pagePosition: Int = 0
) : DisplayItem(R.layout.item_story_full) {

    class ViewHolder(view: View) : DisplayViewHolder<StoryFullDisplay>(view) {
        override fun bind(item: StoryFullDisplay) {
            iv_story_bg.loadColor(item.pages[item.pagePosition])
            tv_story_message.text = "$adapterPosition ${item.pagePosition}"
        }

        private fun ImageView.loadColor(@ColorInt color: Int) {
            Glide.with(this)
                .load(ColorDrawable(color))
                .into(this)
        }
    }
}

class CubePageTransformer : ViewPager2.PageTransformer {
    override fun transformPage(view: View, position: Float) {
        view.pivotX = if (position < 0F) view.width.toFloat() else 0F
        view.pivotY = view.height * 0.5F
        view.rotationY = 45F * position
    }
}
