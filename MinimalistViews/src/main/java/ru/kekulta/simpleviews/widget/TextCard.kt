package ru.kekulta.simpleviews.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.withStyledAttributes
import ru.kekulta.simpleviews.R


class TextCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var title = ""

    var text =
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."

    private lateinit var textLayout: StaticLayout
    private lateinit var titleLayout: StaticLayout
    private var cornerRadius = 0f
    private var rect = RectF()
    private var color: Int = 0
    private var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private lateinit var layoutTextBuilder: StaticLayout.Builder
    private lateinit var layoutTitleBuilder: StaticLayout.Builder
    private var textPaint = TextPaint()
    private var titlePaint = TextPaint()
    private var textPadding = 0f

    init {
        context.withStyledAttributes(attrs, R.styleable.TextCard) {
            title = getString(R.styleable.TextCard_title) ?: ""
            color = getColor(R.styleable.TextCard_background_color, Color.GRAY)
        }


        paint.apply {
            textSize = 25f
            style = Paint.Style.FILL
            textAlign = Paint.Align.LEFT
            typeface = ResourcesCompat.getFont(context, R.font.nunito)
        }
        paint.color = Color.BLACK
        textPaint = TextPaint(paint)

        paint.textSize *= 1.5f
        paint.typeface = ResourcesCompat.getFont(context, R.font.nunitobold)
        titlePaint = TextPaint(paint)
        paint.textSize /= 1.5f
        paint.typeface = ResourcesCompat.getFont(context, R.font.nunito)

        paint.color = color
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        rect = RectF(0f, 0f, width.toFloat(), height.toFloat())


        //println("lines: ${textLayout.lineCount}")
        //println("height: ${textLayout.height}")
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = color
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint)

        if (title.isNotEmpty()) {
            canvas.translate(textPadding, cornerRadius / 2)
            titleLayout.draw(canvas)
            canvas.translate(-textPadding, titleLayout.height.toFloat() - cornerRadius)
        }

        canvas.translate(textPadding, cornerRadius)
        textLayout.draw(canvas)
        canvas.translate(
            -textPadding,
            -cornerRadius - if (title.isNotEmpty()) titleLayout.height else 0
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)


        val width = resolveSize(measuredWidth, widthMeasureSpec)

        cornerRadius = width / 8f
        textPadding = cornerRadius / 2


        val titleHeight = setTextLayout(width)

        var height = (textLayout.height + titleHeight + cornerRadius * 2).toInt()

        height = resolveSize(height, heightMeasureSpec)

        setMeasuredDimension(width, height)
    }

    private fun setTextLayout(width: Int): Int {
        var titleHeight = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            layoutTextBuilder = StaticLayout.Builder.obtain(
                text,
                0,
                text.length,
                textPaint,
                (width - textPadding * 2).toInt()
            )
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .setLineSpacing(0f, 1f)
                .setIncludePad(false)

            textLayout = layoutTextBuilder.build()


            if (title.isNotEmpty()) {
                layoutTitleBuilder = StaticLayout.Builder.obtain(
                    title,
                    0,
                    title.length,
                    titlePaint,
                    (width - textPadding * 2).toInt()
                )
                    .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                    .setLineSpacing(0f, 1f)
                    .setIncludePad(false);

                titleLayout = layoutTitleBuilder.build()

            }
        } else {
            /**
             * Added for API 21 support
             */
            textLayout = StaticLayout(
                text,
                textPaint,
                (width - textPadding * 2).toInt(),
                Layout.Alignment.ALIGN_NORMAL,
                1f,
                0f,
                false
            )
            if (title.isNotEmpty()) {
                titleLayout = StaticLayout(
                    title,
                    titlePaint,
                    (width - textPadding * 2).toInt(),
                    Layout.Alignment.ALIGN_NORMAL,
                    1f,
                    0f,
                    false
                )
            }
        }
        if (title.isNotEmpty()) {
            titleHeight = titleLayout.height - (cornerRadius / 2).toInt()
        }
        return titleHeight
    }

    fun applyChanges() {
        requestLayout()
        invalidate()
    }
}