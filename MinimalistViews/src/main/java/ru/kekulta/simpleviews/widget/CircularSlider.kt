package ru.kekulta.simpleviews.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.withStyledAttributes
import ru.kekulta.simpleviews.R
import java.lang.Math.toRadians
import kotlin.math.min
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

const val SHADOW_COLOR = 0x5F000000

class CircularSlider @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr) {
    private var shadowRect = RectF()
    private var shadowRadius = 0f
    private var offset = 0f
    private var capDiameter = 0f
    private var underWidth = 0f
    private var captured = false
    private var prevX = 0f
    private var capRect = RectF()
    private var prevY = 0f
    private var capX = 0f
    private var capY = 0f
    private var capColor = Color.CYAN
    private var rect = RectF()
    private var indicatorRect = RectF()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.BLACK
    }
    private var indicatorColor = Color.BLACK
    private var underColor = Color.WHITE
    private var indicatorWidth = 0f
    private var degrees: Float = 0f
        // TODO: sometimes slider disappears and position sets to zero. Struggling with reproducing. Should be fixed later
        set(value) {
            field = when {
                value > 360f -> 360f
                value < 0f -> 0f
                else -> value
            }
            calculateCap()
            invalidate()
            onChangeListener?.invoke(field)
        }
    private var fromPosition = 0
    private var toPosition = 60
    private var divider = 0f
    private var minStep = false
    private var onChangeListener: ((Float) -> Unit)? = null


    init {
        context.withStyledAttributes(attrs, R.styleable.CircularSlider) {
            indicatorWidth = getDimension(R.styleable.CircularSlider_indicator_width, -1f)
            underWidth = getDimension(R.styleable.CircularSlider_under_width, -1f)
            capDiameter = getDimension(R.styleable.CircularSlider_cap_radius, -1f)
            indicatorColor = getColor(R.styleable.CircularSlider_indicator_color, Color.BLUE)

            underColor = getColor(R.styleable.CircularSlider_under_color, Color.WHITE)
            capColor = getColor(R.styleable.CircularSlider_cap_color, Color.MAGENTA)

        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        //println("underColor: $underColor")
        if (indicatorWidth == -1f) indicatorWidth = min(width, height) / 10f
        if (underWidth == -1f) underWidth = min(width, height) / 10f
        if (capDiameter == -1f) capDiameter = min(width, height) / 10f
        setLayerType(LAYER_TYPE_SOFTWARE, null)

        shadowRadius = capDiameter / 10f
        offset = max(underWidth, max(capDiameter + shadowRadius * 2, indicatorWidth))

        divider = if (minStep) 360f / (toPosition - fromPosition + 1) else degrees


        rect = RectF(
            0f + offset / 2f,
            0f + offset / 2f,
            width.toFloat() - offset / 2f,
            height.toFloat() - offset / 2f
        )
        indicatorRect = RectF(
            0f + offset / 2f,
            0f + offset / 2f,
            width.toFloat() - offset / 2f,
            height.toFloat() - offset / 2f
        )
        calculateCap()
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.strokeWidth = underWidth
        paint.color = underColor
        paint.style = Paint.Style.STROKE
        canvas.drawOval(rect, paint)

        paint.color = indicatorColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = indicatorWidth
        paint.strokeCap = Paint.Cap.ROUND
        canvas.drawArc(
            indicatorRect,
            -90f,
            degrees - if (minStep) (degrees % divider) else 0f,
            false,
            paint
        )


        paint.setShadowLayer(capDiameter / 10f, 0f, capDiameter / 10, SHADOW_COLOR)
        paint.maskFilter = null
        paint.style = Paint.Style.FILL
        paint.color = capColor
        canvas.drawOval(capRect, paint)
        paint.clearShadowLayer()


    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (parent != null && event.action == MotionEvent.ACTION_DOWN) {
            parent.requestDisallowInterceptTouchEvent(true)
        }
        return super.dispatchTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (getDistance(capX, capY, event.x, event.y) <= capDiameter) {
                    prevX = event.x
                    prevY = event.y
                    captured = true
                    return true
                }

            }
            MotionEvent.ACTION_MOVE -> {
                if (captured) {
                    degrees += angleBetweenTwoVectors(
                        prevX,
                        prevY,
                        event.x,
                        event.y,
                        width / 2f,
                        height / 2f
                    )
                    if (degrees == 0f || degrees == 360f) {
                        prevX = width / 2f - offset / 2f
                        prevY = offset / 2f
                    } else {
                        prevX = event.x
                        prevY = event.y
                    }
                    return true
                }

            }
            else -> captured = false

        }
        return super.onTouchEvent(event)
    }

    private fun calculateCap() {
        capX =
            cos(toRadians((degrees - if (minStep) (degrees % divider) else 0f).toDouble() - 90)).toFloat() * (width / 2f - offset / 2f) + width / 2f
        capY =
            sin(toRadians((degrees - if (minStep) (degrees % divider) else 0f).toDouble() - 90)).toFloat() * (width / 2f - offset / 2f) + height / 2f
        capRect = RectF(
            capX - capDiameter / 2f,
            capY - capDiameter / 2f,
            capX + capDiameter / 2f,
            capY + capDiameter / 2f
        )
        shadowRect = RectF(
            capX - capDiameter / 2f,
            capY - capDiameter / 2f + capDiameter / 5,
            capX + capDiameter / 2f,
            capY + capDiameter / 2f + capDiameter / 5
        )
    }


    fun deleteOnChangeListener() {
        onChangeListener = null
    }

    fun setOnChangeListener(listener: (Float) -> Unit) {
        onChangeListener = listener
    }
}