package ru.kekulta.simpleviews.widget

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.drawable.toBitmap
import ru.kekulta.simpleviews.R
import kotlin.math.max
import kotlin.math.min

private const val RATIO = 120f / 260f

class CardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var startColor: Int = 0
    var endColor: Int = 0
    var gradientStartX = 0f
    var gradientEndX = 0f
    var gradientStartY = 0f
    var gradientEndY = 0f
    var cornerRadius = 0f
    var absoluteTextSize = -1f
    var bottomText = ""
    var centerText = ""
    var topText = ""
    private var textSize = 7f
    private var isSquare = false
    private var autoRatio = false
    private var ratio = 0f
    private var centerTextX = 0f
    private var centerTextY = 0f
    private var bottomTextX = 0f
    private var bottomTextY = 0f
    private var topTextX = 0f
    private var topTextY = 0f
    private var iconRatio = 2f / 4f
    private var iconWidth = 0f
    private var iconHeight = 0f
    private var iconDestinationRect: Rect = Rect(0, 0, 0, 0)
    private var cardWidth = 0f
    private var cardHeight = 0f
    private var rightIcon = false

    private lateinit var gradient: Shader

    var drawable: Drawable? = null
        set(value) {
            field = value
            bitmap = value?.toBitmap()
        }
    var bitmap: Bitmap? = null
        set(value) {
            field = value
            setIconMeasure()
            invalidate()
        }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.LEFT
        typeface = ResourcesCompat.getFont(context, R.font.nunito)
    }

    init {
        context.withStyledAttributes(attrs, R.styleable.CardView) {
            rightIcon = getBoolean(R.styleable.CardView_icon_to_right, false)
            startColor = getColor(R.styleable.CardView_color_start, Color.WHITE)
            endColor = getColor(R.styleable.CardView_color_end, Color.BLACK)
            isSquare = getBoolean(R.styleable.CardView_square_borders, false)
            autoRatio = getBoolean(R.styleable.CardView_auto_ratio, false)
            ratio = getFloat(R.styleable.CardView_ratio, RATIO)
            gradientStartX = getFloat(R.styleable.CardView_gradient_start_x, 0f)
            gradientStartY = getFloat(R.styleable.CardView_gradient_start_y, 1f)
            gradientEndY = getFloat(R.styleable.CardView_gradient_end_y, 0f)
            gradientEndX = getFloat(R.styleable.CardView_gradient_end_x, 1f)
            cornerRadius = getDimension(R.styleable.CardView_corner_radius, -1f)
            topText = getString(R.styleable.CardView_top_text) ?: topText
            bottomText = getString(R.styleable.CardView_bottom_text) ?: bottomText
            textSize = getFloat(R.styleable.CardView_text_size, 7f)
            drawable = getDrawable(R.styleable.CardView_icon)
            iconRatio = getFloat(R.styleable.CardView_iconRatio, iconRatio)
            centerText = getString(R.styleable.CardView_center_text) ?: centerText
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        cardWidth = width.toFloat()
        cardHeight = height.toFloat()


        if (cornerRadius == -1f) cornerRadius = cardWidth / 10f
        //println("textSize: $textSize, cardHeight: $cardHeight")
        if (cardHeight != 0f) textSize = cardHeight / textSize
        if (absoluteTextSize != -1f) textSize = absoluteTextSize
        paint.textSize = textSize


        gradient = LinearGradient(
            cardWidth * gradientStartX, cardHeight * gradientStartY, cardWidth * gradientEndX,
            cardHeight * gradientEndY, startColor, endColor, Shader.TileMode.MIRROR
        )

        setIconMeasure()

        bottomTextX = 0f + cornerRadius / 2
        centerTextX = 0f + cornerRadius / 2
        topTextX = 0f + cornerRadius / 2
        bottomTextY = cardHeight - cornerRadius / 2
        centerTextY = height / 2f + textSize / 3f
        topTextY = 0 + cornerRadius / 2 + textSize
    }

    private fun setIconMeasure() {
        // TODO set custom icon-to-view ratio
        if (bitmap == null) return

        val size = min(cardHeight, cardWidth)
        iconHeight = size * iconRatio
        iconWidth = size * iconRatio

        var iconModifier = 0f
        if (rightIcon) {
            iconModifier = max(cardWidth / 2f - cornerRadius - iconWidth / 2f, 0f)
        }

        val iconLeft = (cardWidth / 2f - iconWidth / 2f + iconModifier).toInt()
        val iconTop = (cardHeight / 2f - iconHeight / 2f).toInt()
        val iconRight = (cardWidth / 2f + iconWidth / 2f + iconModifier).toInt()
        val iconBottom = (cardHeight / 2f + iconHeight / 2f).toInt()
        iconDestinationRect = Rect(iconLeft, iconTop, iconRight, iconBottom)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.shader = gradient

        canvas.drawRoundRect(
            0f,
            0f,
            cardWidth,
            cardHeight,
            cornerRadius,
            cornerRadius,
            paint
        )



        paint.shader = null
        paint.color = Color.BLACK
        bitmap?.let {
            canvas.drawBitmap(it, null, iconDestinationRect, paint)
        }
        canvas.drawText(topText, topTextX, topTextY, paint)
        canvas.drawText(centerText, centerTextX, centerTextY, paint)
        canvas.drawText(bottomText, bottomTextX, bottomTextY, paint)


    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val width = resolveSize(measuredWidth, widthMeasureSpec)
        val height = resolveSize(measuredHeight, heightMeasureSpec)
        when {
            isSquare -> setMeasuredDimension(width, width)
            autoRatio -> setMeasuredDimension(width, (width * ratio).toInt())
            else -> setMeasuredDimension(width, height)
        }
    }

    override fun invalidate() {
        gradient = LinearGradient(
            cardWidth * gradientStartX, cardHeight * gradientStartY, cardWidth * gradientEndX,
            cardHeight * gradientEndY, startColor, endColor, Shader.TileMode.MIRROR
        )

        if (absoluteTextSize != -1f) textSize = absoluteTextSize
        paint.textSize = textSize

        super.invalidate()
    }
}