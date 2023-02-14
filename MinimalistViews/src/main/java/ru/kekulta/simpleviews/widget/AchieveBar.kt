package ru.kekulta.simpleviews.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.withStyledAttributes
import ru.kekulta.simpleviews.R

import kotlin.math.min


class AchieveBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttribute: Int = 0,
) : View(context, attrs, defStyleAttribute) {
    var position = 0
        set(value) {
            stages.let { stages ->
                if (stages == null || value < 0 || value > stages.size) throw IllegalArgumentException(
                    "You can't set position beyond provided stages list"
                )
            }
            field = value
            invalidate()
        }

    private var stageStroke = 0f
    private var startOffset: Float = 0f
    private var stageY = 0f
    private var endOffset: Float = 0f
    private var stageDiameter: Float = 0f
    private var stagesWidth: Float = 0f
    private var stagesGapWidth: Float = 0f
    private var cornerRadius = 0f
    private var strokeCornerRadius = 0f
    private var stageTextSize = 0f
    private var title = "Your Habit!"
    private var stages: MutableList<Pair<String, Int>>? =
        MutableList(7) { Pair(dayOfWeek(it), it + 1) }
    private var startColor = 0
    private var endColor = 0
    private var stageColor = 0
    private var strokeWidth = 0f
    private var strokeColor = 0
    private var indicatorRect = RectF(0f, 0f, 0f, 0f)
    private var strokeRect = RectF(0f, 0f, 0f, 0f)
    private var titleTextSize = 0f
    private var titleX = 0f
    private var titleY = 0f
    private var paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER

    }

    private lateinit var gradient: Shader

    init {
        context.withStyledAttributes(attrs, R.styleable.AchieveBar) {
            cornerRadius = getDimension(R.styleable.AchieveBar_corner_radius, -1f)
            strokeColor = getColor(R.styleable.AchieveBar_stroke_color, Color.GRAY)
            strokeWidth = getDimension(R.styleable.AchieveBar_stroke_width, -1f)
            startColor = getColor(R.styleable.AchieveBar_color_start, Color.WHITE)
            endColor = getColor(R.styleable.AchieveBar_color_end, Color.BLACK)
            stageColor = getColor(R.styleable.AchieveBar_stage_color, Color.GRAY)
            position = getInteger(R.styleable.AchieveBar_position, 0)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        //println(cornerRadius)
        //TODO this would save old cornerRadius and strokeWidth if size actually changed
        if (cornerRadius == -1f) cornerRadius = width / 10f
        if (strokeWidth == -1f) strokeWidth = min(height, width) / 150f
        strokeCornerRadius = cornerRadius - strokeWidth / 2

        titleTextSize = height / 8f

        titleX = width / 2f
        titleY = titleTextSize + cornerRadius / 2 + strokeWidth

        stages?.let { stages ->
            startOffset = cornerRadius * 2f
            endOffset = cornerRadius * 1.5f
            stagesWidth = width.toFloat() - startOffset - endOffset
            stageDiameter = min(
                height / 5f,
                stagesWidth / (stages.size * 2 - 1)
            ) * 1.2f

            stagesGapWidth =
                (stagesWidth - stages.size * stageDiameter) / if (stages.size > 1) stages.size - 1 else 1

            stageY = height - height / 5f
            stageStroke = stageDiameter / 10f
            stageTextSize = stageDiameter / 2f

            setUpIndicator()
        }





        strokeRect = RectF(
            strokeWidth / 2,
            strokeWidth / 2,
            width.toFloat() - strokeWidth / 2,
            height.toFloat() - strokeWidth / 2

        )

    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)


        paint.shader = gradient
        paint.style = Paint.Style.FILL

        stages?.let { stages ->
            canvas.drawRoundRect(indicatorRect, cornerRadius, cornerRadius, paint)
            paint.shader = null
            paint.strokeWidth = stageStroke
            paint.typeface = ResourcesCompat.getFont(context, R.font.nunitobold)
            paint.textSize = stageTextSize
            stages.forEachIndexed { index, value ->
                paint.style =
                    if (index >= position) Paint.Style.STROKE else Paint.Style.FILL_AND_STROKE
                paint.color = stageColor
                canvas.drawOval(
                    startOffset + stagesGapWidth * index + stageDiameter * index,
                    stageY - stageDiameter,
                    startOffset + stageDiameter + stagesGapWidth * index + stageDiameter * index,
                    stageY,
                    paint
                )
                paint.color = Color.BLACK
                paint.style = Paint.Style.FILL
                canvas.drawText(
                    value.first,
                    startOffset + stagesGapWidth * index + stageDiameter * index + stageDiameter / 2f,
                    stageY - stageDiameter * 2f + stageTextSize / 3f,
                    paint
                )

                canvas.drawText(
                    value.second.toString(),
                    startOffset + stagesGapWidth * index + stageDiameter * index + stageDiameter / 2f,
                    stageY - stageDiameter / 2 + stageTextSize / 3f,
                    paint
                )
            }
        }

        paint.shader = null
        paint.color = strokeColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = strokeWidth
        canvas.drawRoundRect(strokeRect, cornerRadius, strokeCornerRadius, paint)


        paint.typeface = ResourcesCompat.getFont(context, R.font.nunitobold)
        paint.style = Paint.Style.FILL
        paint.color = Color.BLACK
        paint.textSize = titleTextSize
        canvas.drawText(title.uppercase(), titleX, titleY, paint)


    }

    override fun invalidate() {
        setUpIndicator()
        super.invalidate()
    }

    private fun setUpIndicator() {
        stages?.let { stages ->
            val indicatorRight = when (position) {
                0 -> startOffset - stageStroke / 2f
                stages.size -> width.toFloat() - strokeWidth / 2
                else -> startOffset + stagesGapWidth * position + stageDiameter * position - stageStroke / 2f
            }

            indicatorRect = RectF(
                strokeWidth / 2,
                strokeWidth / 2,
                indicatorRight,
                height.toFloat() - strokeWidth / 2
            )

            gradient = LinearGradient(
                0f,
                height / 2f,
                indicatorRight,
                height / 2f,
                startColor,
                endColor,
                Shader.TileMode.MIRROR
            )

        }
    }
}
