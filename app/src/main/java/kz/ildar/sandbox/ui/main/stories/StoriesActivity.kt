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

import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.customview.widget.ViewDragHelper
import androidx.viewpager2.widget.ViewPager2
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrConfig
import com.r0adkll.slidr.model.SlidrListener
import com.r0adkll.slidr.model.SlidrPosition
import com.teresaholfeld.stories.StoriesProgressView
import kotlinx.android.synthetic.main.activity_stories.*
import kz.ildar.sandbox.R
import kz.ildar.sandbox.utils.ext.observe
import kz.ildar.sandbox.utils.ext.orDefault
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import kotlin.math.abs

/**
 * Экран просмотра сториз
 */
class StoriesActivity : AppCompatActivity() {
    private val viewModel: StoriesViewModel by viewModel()
    private val storiesAdapter = StoriesAdapter()

    private var storyCount: Int = -1
    private var storyPosition: Int = -1
    private var pagesCount: Int = -1
    private var pagePosition = -1

    private var pressTime = 0L
    private val limit = 500L
    private var leftSide = true
    private var mIsDragging = false
    private var mIsVerticalDragging = false
    private var lastX = 0f
    private var mTouchSlop = 0

    /**
     * Ставим сториз на паузу, когда юзер держит палец на экране: [indicator]
     * Определяем клики: [pressTime] & [limit]
     * Определяем, на какой стороне кликнул юзер: [leftSide]
     * Управляем перетаскиванием [story_container]: [mIsDragging] & [lastX]
     */
    private val onTouchListener = View.OnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                indicator.pause()
                pressTime = System.currentTimeMillis()
                lastX = event.x
                return@OnTouchListener false
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = event.x - lastX

                if (!mIsDragging) {
                    if (abs(deltaX) > mTouchSlop) {
                        mIsDragging = true
                    }
                }

                if (mIsDragging) {
                    lastX = event.x
                    if (story_container.isFakeDragging || story_container.beginFakeDrag()) {
                        story_container.fakeDragBy(deltaX)
                    }
                }
            }
            MotionEvent.ACTION_CANCEL,
            MotionEvent.ACTION_UP -> {
                if (event.action != MotionEvent.ACTION_CANCEL) {
                    indicator.resume()
                }
                val width = v.width
                leftSide = event.x < width / 2

                if (!mIsDragging
                    && event.action != MotionEvent.ACTION_CANCEL
                    && System.currentTimeMillis() - pressTime < limit
                ) {
                    return@OnTouchListener v.performClick()
                }
                mIsDragging = false
                if (story_container.isFakeDragging) {
                    story_container.endFakeDrag()
                }
                return@OnTouchListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stories)
        setupSwipeClose()
        setupTouchArea()
        setupPager()
    }

    private fun setupTouchArea() {
        mTouchSlop = ViewConfiguration.get(this).scaledPagingTouchSlop
        touch_area.setOnClickListener {
            if (leftSide) {
                if (pagePosition > 0) indicator.reverse()
                else goLeft()
            } else indicator.skip()
        }
        touch_area.setOnTouchListener(onTouchListener)
        button_stories.setOnClickListener {
            viewModel.loadStories()
        }
    }

    private fun setupPager() {
        story_container.adapter = storiesAdapter
        story_container.setPageTransformer(CubePageTransformer())
        story_container.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position > storyPosition) goRight(false)
                if (position < storyPosition) goLeft(false)
            }
        })

        observe(viewModel.storiesListLiveData) {
            storiesAdapter.stories = it
            storyCount = it.size
            storyPosition = 0
            pagesCount = it.firstOrNull()?.pageCount.orDefault(-1)
            pagePosition = it.firstOrNull()?.pagePosition.orDefault(-1)
            button_stories.text = "loaded stories: $storyCount"
            resetIndicator()
        }
    }

    private fun goLeft(updatePager: Boolean = true) {
        if (storyPosition <= 0) {
            indicator.reverse()
            return
        }
        pagesCount = storiesAdapter.stories[--storyPosition].pageCount
        pagePosition = storiesAdapter.stories[storyPosition].pagePosition
        button_stories.text = "started left $storyPosition $pagePosition"
        if (updatePager) story_container.currentItem = storyPosition
        resetIndicator()
    }

    private fun goRight(updatePager: Boolean = true) {
        if (storyPosition == storyCount - 1) {
            finish()
            return
        }
        pagesCount = storiesAdapter.stories[++storyPosition].pageCount
        pagePosition = storiesAdapter.stories[storyPosition].pagePosition
        button_stories.text = "started right $storyPosition $pagePosition"
        if (updatePager) story_container.currentItem = storyPosition
        resetIndicator()
    }

    /**
     * Верхний индикатор сториз
     */
    private fun resetIndicator() {
        indicator.clear()
        indicator.setStoriesListener(object : StoriesProgressView.StoriesListener {
            override fun onComplete() {
                goRight()
            }

            override fun onNext() {
                pagePosition++
                button_stories.text = "next: $storyPosition $pagePosition"
                storiesAdapter.stories[storyPosition].pagePosition = pagePosition
                storiesAdapter.notifyItemChanged(storyPosition)
            }

            override fun onPrev() {
                if (pagePosition > 0) pagePosition--
                button_stories.text = "previous: $storyPosition $pagePosition"
                storiesAdapter.stories[storyPosition].pagePosition = pagePosition
                storiesAdapter.notifyItemChanged(storyPosition)
            }
        })
        indicator.setStoriesCount(pagesCount)
        indicator.setStoryDuration(5000L)
        if (!mIsVerticalDragging) {
            indicator.startStories(pagePosition)
        }
    }

    /**
     * Закрытие окна по свайпу вниз
     */
    private fun setupSwipeClose() {
        (window.decorView as? ViewGroup)?.getChildAt(0)
            ?.setBackgroundColor(resources.getColor(R.color.bg_stories))

        val config = SlidrConfig.Builder()
//            .primaryColor(resources.getColor(R.color.colorPrimary))
//            .secondaryColor(resources.getColor(R.color.colorAccent))
            .position(SlidrPosition.TOP)
            .sensitivity(.2f)//как долго слайдер не реагирует на свайп вниз
            .scrimColor(Color.BLACK)
            .scrimStartAlpha(0.8f)
            .scrimEndAlpha(0f)
            .velocityThreshold(1000f)
            .distanceThreshold(0.25f)
            .listener(object : SlidrListener {
                override fun onSlideStateChanged(state: Int) {
                    mIsVerticalDragging = state == ViewDragHelper.STATE_DRAGGING
                }

                override fun onSlideChange(percent: Float) {
                    //1 -> 0
                    //1 = еще не тронут
                    //0.5 = свайпнут наполовину
                    //0 = полностью свайпнут вниз
                }

                /**
                 * юзер передумал и отпустил
                 */
                override fun onSlideOpened() {
                    val running = try {
                        val current = StoriesProgressView::class.java.declaredFields.find {
                            it.name == "current"
                        }?.also {
                            it.isAccessible = true
                        }?.get(indicator) as? Int ?: -1

                        current >= 0
                    } catch (e: Exception) {
                        Timber.w(e, "StoriesProgressView reflection failed")
                        false
                    }

                    if (running) {
                        indicator.resume()
                    } else {
                        //rare case: swipe left/right then bottom
                        indicator.startStories(pagePosition)
                    }
                    button_stories.text = "resume: $storyPosition $pagePosition"
                }

                /**
                 * юзер полностью свайпнул
                 */
                override fun onSlideClosed(): Boolean {
                    return false
                }
            })
            .build()

        Slidr.attach(this, config)
    }

    override fun onDestroy() {
        super.onDestroy()
        indicator.destroy()
        story_container.adapter = null
    }
}
