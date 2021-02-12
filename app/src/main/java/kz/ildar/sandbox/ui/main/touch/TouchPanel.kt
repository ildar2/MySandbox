package kz.ildar.sandbox.ui.main.touch

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.Keep
import kz.ildar.sandbox.ui.main.touch.TouchPanel.Mode
import timber.log.Timber
import java.util.*
import kotlin.math.min
import kotlin.random.Random

/**
 * Can combine three colors based on user touch positions
 * @see Mode
 */
class TouchPanel @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        const val MAX_TARGET_SIZE = 4
    }

    private val linesPaint = Paint()
    private val rectPaint = Paint()
    private val outlinePaint = Paint()
    var logger: (String) -> Unit = {}
    private var mode: Mode = Mode.WHITE

    init {
        linesPaint.color = Color.GRAY
        linesPaint.strokeWidth = 0f
        outlinePaint.strokeWidth = 2f
        outlinePaint.style = Paint.Style.STROKE
        outlinePaint.isAntiAlias = true
    }

    /**
     * this view location to adjust raw X & Y
     */
    private val location = intArrayOf(0, 0)
    private var minSide = 0f

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        getLocationOnScreen(location)
        minSide = min(measuredWidth, measuredHeight).toFloat()
    }

    private var currentEvent: MotionEvent? = null
    private var pressTime = 0L
    private val clickLimit = 500L

    override fun onTouchEvent(event: MotionEvent): Boolean {
        currentEvent = event
        mode = calcMode(event)
        calcCollisions(event)
        when (event.actionMasked) {
            MotionEvent.ACTION_UP -> {
                currentEvent = null
                mode = Mode.WHITE
                if (System.currentTimeMillis() - pressTime < clickLimit) {
                    performClick()
                }
            }
            MotionEvent.ACTION_DOWN -> {
                pressTime = System.currentTimeMillis()
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                pressTime = System.currentTimeMillis()
            }
            MotionEvent.ACTION_POINTER_UP -> {
                if (System.currentTimeMillis() - pressTime < clickLimit) {
                    performClick()
                }
            }
        }
        logger(mode.name)
        invalidate()
        return true
    }

    override fun performClick(): Boolean {
        Timber.i("clicked")
        return super.performClick()
    }

    private fun calcMode(event: MotionEvent): Mode {
        var isRed = false
        var isGreen = false
        var isBlue = false
        //track pointer locations
        for (i in 0 until event.pointerCount) {
            val x = event.getX(i)
            val y = event.getY(i)
            if (y > borderBottom) {
                when {
                    i == event.actionIndex && event.actionMasked == MotionEvent.ACTION_POINTER_UP -> {
                        //exclude ACTION_POINTER_UP from mode calculation
                    }
                    x > thirdWidth - delta && x < thirdWidth + delta -> {
                        //one finger on the border of red and green
                        isRed = true
                        isGreen = true
                    }
                    x > twoThirdsWidth - delta && x < twoThirdsWidth + delta -> {
                        //one finger on the border of green and blue
                        isBlue = true
                        isGreen = true
                    }
                    x > twoThirdsWidth -> {
                        isBlue = true
                    }
                    x < thirdWidth -> {
                        isRed = true
                    }
                    else -> {
                        isGreen = true
                    }
                }
            }
        }
        return Mode.calc(isRed, isGreen, isBlue)
    }

    private val delta: Float
        get() = width * 0.02f

    private val sizeMultiplier: Float
        get() = minSide * 0.03f

    private val thirdWidth: Float
        get() = width * 0.333f

    private val twoThirdsWidth: Float
        get() = width * 0.667f

    private val borderTop: Float
        get() = height * 0.1f

    private val borderBottom: Float
        get() = height * 0.75f

    override fun onDraw(canvas: Canvas?) {
        val height = height.toFloat()
        val width = width.toFloat()

        rectPaint.color = mode.color
        canvas?.drawRect(0f, 0f, width, height * 0.1f, rectPaint)

        rectPaint.color = Mode.RED.color
        canvas?.drawRect(0f, borderBottom, thirdWidth, height, rectPaint)
        rectPaint.color = Mode.GREEN.color
        canvas?.drawRect(thirdWidth, borderBottom, twoThirdsWidth, height, rectPaint)
        rectPaint.color = Mode.BLUE.color
        canvas?.drawRect(twoThirdsWidth, borderBottom, width, height, rectPaint)

        //show available targets at the top
        val grouped = targets.groupBy {
            it.mode == mode
        }
        grouped[false]?.forEach {
            canvas?.drawTarget(it)
        }
        grouped[true]?.forEach {
            canvas?.drawTarget(it)
        }

        val debug = false
        if (debug) {
            currentEvent?.let { event ->
                for (i in 0 until event.pointerCount) {
                    if (i == event.actionIndex && event.actionMasked == MotionEvent.ACTION_POINTER_UP) continue
                    val x = event.getX(i) - location[0]
                    val y = event.getY(i) - location[1]
                    canvas?.drawLine(0f, y, width, y, linesPaint)//horizontal
                    canvas?.drawLine(x, 0f, x, height, linesPaint)//vertical
                }
            }
        }

        super.onDraw(canvas)
    }

    private fun Canvas.drawTarget(it: TouchTarget) {
        rectPaint.color = it.mode.color
        val x = it.getRawX()
        val y = it.getRawY()
        if (it.mode == Mode.WHITE) {
            this.drawCircle(x, y, it.size * sizeMultiplier, outlinePaint)
        }
        this.drawCircle(x, y, it.size * sizeMultiplier, rectPaint)
    }

    @Keep
    enum class Mode(
        val color: Int,
        var hasRed: Boolean = false,
        var hasGreen: Boolean = false,
        var hasBlue: Boolean = false,
    ) {
        WHITE(Color.WHITE),
        RED(Color.rgb(224, 48, 22), true),
        GREEN(Color.rgb(12, 255, 32), hasGreen = true),
        BLUE(Color.rgb(12, 36, 238), hasBlue = true),
        YELLOW(Color.YELLOW, true, hasGreen = true),
        CYAN(Color.CYAN, hasGreen = true, hasBlue = true),
        MAGENTA(Color.MAGENTA, true, hasBlue = true),
        BLACK(Color.BLACK, true, true, true);

        companion object {
            fun calc(
                hasRed: Boolean = false,
                hasGreen: Boolean = false,
                hasBlue: Boolean = false,
            ) = values().find {
                it.hasRed == hasRed && it.hasGreen == hasGreen && it.hasBlue == hasBlue
            } ?: WHITE
        }
    }

    private var targets = ArrayList<TouchTarget>().apply {
        fun ArrayList<TouchTarget>.addRandomTarget() {
            val random = Random(System.currentTimeMillis())
            val element = TouchTarget(
                Mode.values()[random.nextInt(Mode.values().size)],
                random.nextInt(1, MAX_TARGET_SIZE + 1),
                random.nextFloat(),
                random.nextFloat()
            )
            Timber.w("gen $element")
            add(element)
        }
        for (i in 1..15) addRandomTarget()
    }

    private fun calcCollisions(event: MotionEvent) {
        for (i in 0 until event.pointerCount) {
            val x = event.getX(i)
            val y = event.getY(i)
            targets.filter {
                it.mode == mode
            }.forEach {
                val radius = it.size * sizeMultiplier
                val xDistance = it.getRawX() - x
                val yDistance = it.getRawY() - y
                val inside = xDistance * xDistance + yDistance * yDistance <= radius * radius
                if (inside) {
                    targets.remove(it)
                }
            }
        }
    }

    private fun TouchTarget.getRawX() = (width - 2 * MAX_TARGET_SIZE * sizeMultiplier) *
        xPercent + MAX_TARGET_SIZE * sizeMultiplier

    private fun TouchTarget.getRawY() = (borderBottom - borderTop - 2 * MAX_TARGET_SIZE * sizeMultiplier) *
        yPercent + borderTop + MAX_TARGET_SIZE * sizeMultiplier
}