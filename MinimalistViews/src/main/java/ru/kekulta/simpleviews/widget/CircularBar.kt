package ru.kekulta.simpleviews.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.withStyledAttributes
import ru.kekulta.simpleviews.R
import kotlin.math.max
import kotlin.math.min

class CircularBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttribute: Int = 0
) : View(context, attrs, defStyleAttribute) {

    private var paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        typeface = ResourcesCompat.getFont(context, R.font.nunitobold)
        textAlign = Paint.Align.CENTER
        color = Color.GRAY
        style = Paint.Style.STROKE
    }
    private var rect = RectF(0f, 0f, 0f, 0f)
    private var underColor = 0
    private var underStrokeWidth = 0f
    private var dot = RectF(0f, 0f, 0f, 0f)
    private var indicatorColor = 0
    private var indicatorWidth = 0f
    private var textSize = 0f
    private var textY = 0f
    var progress = 0f
        // TODO change 0f-1f range to 0-360 or something else to unify with CircularSlider
        set(value) {
            field = max(min(value, 1f), 0f)
            //println("progress: $field")
            invalidate()
        }

    init {
        context.withStyledAttributes(attrs, R.styleable.CircularBar) {
            underColor = getColor(R.styleable.CircularBar_underColor, Color.GRAY)
            underStrokeWidth = getDimension(R.styleable.CircularBar_underWidth, -1f)
            indicatorColor = getColor(R.styleable.CircularBar_indicatorColor, Color.BLACK)
            indicatorWidth = getDimension(R.styleable.CircularBar_indicatorWidth, underStrokeWidth)
            progress = getFloat(R.styleable.CircularBar_progress, 0f)
        }
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        if (underStrokeWidth == -1f) underStrokeWidth = width / 10f
        if (indicatorWidth == -1f) indicatorWidth = underStrokeWidth

        dot = RectF(
            width / 2f - max(underStrokeWidth, indicatorWidth) / 2,
            0f,
            width / 2f + max(underStrokeWidth, indicatorWidth) / 2,
            max(underStrokeWidth, indicatorWidth)
        )

        rect = RectF(
            0f + max(underStrokeWidth, indicatorWidth) / 2,
            0f + max(underStrokeWidth, indicatorWidth) / 2,
            width.toFloat() - max(underStrokeWidth, indicatorWidth) / 2,
            height.toFloat() - max(underStrokeWidth, indicatorWidth) / 2
        )

        textSize = ((rect.bottom - max(
            underStrokeWidth,
            indicatorWidth
        )) - (rect.top + max(underStrokeWidth, indicatorWidth))) / 3
        textY = height / 2f + textSize / 3f

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.strokeCap = Paint.Cap.ROUND

        paint.strokeWidth = underStrokeWidth
        paint.color = underColor
        paint.style = Paint.Style.STROKE
        canvas.drawArc(rect, -90f, 360f, false, paint)



        paint.color = indicatorColor
        paint.strokeWidth = indicatorWidth
        canvas.drawArc(rect, -90f, 360f * progress, false, paint)

        paint.style = Paint.Style.FILL
        canvas.drawOval(dot, paint)

        paint.color = Color.BLACK
        paint.textSize = textSize
        canvas.drawText("${(progress * 100).toInt()}%", width / 2f, textY, paint)


    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val size = min(measuredHeight, measuredWidth)
        setMeasuredDimension(size, size)
    }
}