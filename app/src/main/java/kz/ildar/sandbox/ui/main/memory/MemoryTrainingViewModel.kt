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
package kz.ildar.sandbox.ui.main.memory

import android.view.View
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kz.ildar.sandbox.R
import kz.ildar.sandbox.ui.BaseViewModel
import kz.ildar.sandbox.utils.DisplayItem
import kz.ildar.sandbox.utils.DisplayViewHolder
import kz.ildar.sandbox.utils.EventWrapper
import kz.ildar.sandbox.utils.wrapEvent
import kotlin.math.min

class MemoryTrainingViewModel : BaseViewModel() {

    private val gridSize: Int = 4
    private val showDelay: Long = 700

    /**
     * Количество элементов в последовательности
     */
    private val difficulty: Int = 5

    val stateLiveData = MutableLiveData(TrainingState.INIT)

    private var state: TrainingState = TrainingState.INIT
        set(value) {
            field = value
            stateLiveData.value = value
        }
    private val sequence: ArrayList<Point> = ArrayList()
    private val inputSequence: ArrayList<Point> = ArrayList()

    private var points: ArrayList<PointDisplay> = arrayListOf()
    val pointLiveData = MutableLiveData<List<PointDisplay>>()
    val pointUpdateEvent = MutableLiveData<EventWrapper<Int>>()

    init {
        for (i in 0 until gridSize * gridSize) {
            points.add(
                PointDisplay(Point(i), PressedState.DEFAULT, this::click)
            )
        }
        pointLiveData.value = points
    }

    private fun updatePoint(point: Point?) {
        scope.launch(coroutineProvider.Main) {
            pointUpdateEvent.value = point?.index?.wrapEvent()
        }
    }

    fun start(difficulty: Int = this.difficulty) {
        state = TrainingState.PRODUCING
        uiCaller.makeRequest({
            generate(difficulty)
            for (point in sequence) {
                highlight(point)
                delay(showDelay)
            }
        }) {
            state = TrainingState.RECEIVING
            highlight(null)
        }
    }

    private fun click(point: Point) {
        if (state != TrainingState.RECEIVING) return
        //validate point
        inputSequence.add(point)
        val i = inputSequence.lastIndex
        if (sequence[i] != point) {
            state = TrainingState.ERROR
            setPointState(sequence[i], PressedState.SHOW)
            setPointState(point, PressedState.ERROR)
            updatePoint(sequence[i])
            updatePoint(point)
            return
        }
        setPointState(point, PressedState.PRESSED)
        updatePoint(point)
        //check for ending
        if (inputSequence.size == sequence.size) {
            state = TrainingState.END
            uiCaller.makeRequest({
                delay(1000)
            }) {
                state = TrainingState.INIT
                highlight(null)
            }
        }
    }

    private fun highlight(point: Point?) {
        points.forEach {
            if (point == it.wrapped) {
                it.pressed = PressedState.SHOW
                updatePoint(it.wrapped)
            } else if (it.pressed != PressedState.DEFAULT) {
                it.pressed = PressedState.DEFAULT
                updatePoint(it.wrapped)
            }
        }
    }

    private fun setPointState(point: Point?, state: PressedState) {
        points.find {
            it.wrapped == point
        }?.pressed = state
    }

    private fun generate(difficulty: Int) {
        sequence.clear()
        inputSequence.clear()
        val possible = IntArray(gridSize * gridSize) { it }
        possible.shuffle()
        val size = min(difficulty, gridSize * gridSize)
        for (i in 0 until size) {
            sequence.add(Point(possible[i]))
        }
    }
}

enum class TrainingState {
    INIT,
    PRODUCING,
    RECEIVING,
    ERROR,
    END,
}

/**
 * Обозначает точку в сетке возможных значений
 * пустые значения означают дефолтное состояние, когда ни одна точка не нажата
 */
class Point(
    val index: Int? = null,
) {
    override fun equals(other: Any?): Boolean = other is Point && other.index == index
    override fun hashCode(): Int = index ?: 0
}

class PointDisplay(
    val wrapped: Point,
    var pressed: PressedState = PressedState.DEFAULT,
    val click: ((Point) -> Unit)? = null
) : DisplayItem(R.layout.item_point_display) {

    class PointViewHolder(view: View) : DisplayViewHolder<PointDisplay>(view) {
        override fun bind(item: PointDisplay) {
            val point_icon = containerView.findViewById<ImageView>(R.id.point_icon)
            point_icon.setImageResource(item.pressed.icon)
            point_icon.setOnClickListener {
                item.click?.invoke(item.wrapped)
            }
        }
    }

    fun copy() = PointDisplay(
        wrapped,
        pressed,
        click
    )
}

enum class PressedState(val icon: Int) {
    DEFAULT(R.drawable.ic_point_default),
    PRESSED(R.drawable.ic_point_pressed),
    SHOW(R.drawable.ic_point_show),
    ERROR(R.drawable.ic_point_error),
}