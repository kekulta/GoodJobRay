package ru.kekulta.simpleviews.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.drawable.toBitmap
import ru.kekulta.simpleviews.R

private const val RATIO = 150.0 / 260.0

class ProfileView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttribute: Int = 0,
) : View(context, attrs, defStyleAttribute) {

    private var imageRadius: Float = 0f

    private lateinit var advances: FloatArray
    private lateinit var gradient: Shader
    private lateinit var imagePosition: Rect
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        typeface = ResourcesCompat.getFont(context, R.font.nunitobold)
    }

    var text = "Hello there!"
        set(value) {
            isUnderlined = false
            field = value
            invalidate()
        }
    var drawable: Drawable = ContextCompat.getDrawable(
        context, R.drawable.ic_launcher_background
    )!!
        set(value) {
            field = value
            bitmap = drawable.toBitmap()
        }

    var bitmap: Bitmap = getCroppedBitmap(drawable.toBitmap())
        set(value) {
            field = getCroppedBitmap(value)
            invalidate()
        }
    var imageClickable = false
        set(value) {
            if (!value) onPictureClickListeners = null
            field = value
        }
    var imageLongClickable = false
        set(value) {
            if (!value) onPictureLongClickListeners = null
            field = value
        }


    private var startColor = 0
    private var endColor = 0
    private var cornerRadius = 0f
    private var verticalOffset = 0f
    private var horizontalOffset = 0f

    private var textOffset = 0f
    private var textSize = 100f
    private var textLength = 0f
    private var textStartUnderlinedPositionPixels = 0f
    private var textEndUnderlinedPositionPixels = 0f
    private var underlineOffset = 0f
    private var underlineThickness = 0f
    private var firstUnderlineIndex = 2
    private var lastUnderlineIndex = text.length - 3
    private var isUnderlined = true
    private var textStartPositionPixels: Float = 0f
    private var textEndPositionPixels: Float = 0f
    private var onPictureClickListeners: (() -> Boolean)? = null
    private var onPictureLongClickListeners: (() -> Boolean)? = null
    private lateinit var event: MotionEvent


    init {
        context.withStyledAttributes(attrs, R.styleable.ProfileView) {
            startColor = getColor(R.styleable.ProfileView_colorStart, Color.WHITE)
            endColor = getColor(R.styleable.ProfileView_colorEnd, Color.BLACK)
            text = getString(R.styleable.ProfileView_text) ?: text
            drawable = getDrawable(R.styleable.ProfileView_image) ?: drawable
            firstUnderlineIndex = getInteger(R.styleable.ProfileView_firstUnderlinedIndex, 0)
            lastUnderlineIndex =
                getInteger(R.styleable.ProfileView_lastUnderlinedIndex, text.lastIndex)
            isUnderlined = getBoolean(R.styleable.ProfileView_isUnderlined, false)
            cornerRadius = getDimension(R.styleable.ProfileView_cornerRadius, -1f)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        if (cornerRadius == -1f) cornerRadius = width / 10f
        gradient = LinearGradient(
            0f, height.toFloat(), width.toFloat(), 0f, startColor, endColor, Shader.TileMode.MIRROR
        )
        verticalOffset = height / 3f
        horizontalOffset = width / 2f


        val padding = verticalOffset / 10f
        imageRadius = verticalOffset - padding
        val imageLeft = (horizontalOffset - imageRadius).toInt()
        val imageTop = (verticalOffset - imageRadius).toInt()
        val imageRight = (horizontalOffset + imageRadius).toInt()
        val imageBottom = (verticalOffset + imageRadius).toInt()
        imagePosition = Rect(imageLeft, imageTop, imageRight, imageBottom)


        textSize = (height - verticalOffset) / 7f
        textOffset = (verticalOffset * 2 + textSize)
        paint.textSize = textSize

        if (isUnderlined) setUnderlineMeasure()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.shader = gradient

        canvas.drawRoundRect(
            0f, verticalOffset, width.toFloat(), height.toFloat(), cornerRadius, cornerRadius, paint
        )
        canvas.drawCircle(horizontalOffset, verticalOffset, verticalOffset, paint)

        canvas.drawBitmap(bitmap, null, imagePosition, paint)

        paint.shader = null
        paint.color = Color.BLACK
        paint.strokeWidth = underlineThickness
        canvas.drawText(text, horizontalOffset, textOffset, paint)

        if (isUnderlined) {
            canvas.drawLine(
                textStartUnderlinedPositionPixels,
                underlineOffset,
                textEndUnderlinedPositionPixels,
                underlineOffset,
                paint
            )
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = measuredWidth
        val height = (width * RATIO).toInt()
        setMeasuredDimension(width, height)
    }

    override fun invalidate() {
        onSizeChanged(width, height, width, height)
        super.invalidate()
    }

    private fun setUnderlineMeasure() {
        advances = FloatArray(text.length)
        paint.getTextWidths(text, advances)
        textLength = advances.sum()
        underlineOffset = textOffset + textSize / 10f
        underlineThickness = (underlineOffset - textOffset) / 2f
        textStartPositionPixels = horizontalOffset - textLength / 2
        textEndPositionPixels = horizontalOffset + textLength / 2
        var startUnderlineOffsetPixels = 0f
        for (i in 0 until firstUnderlineIndex) {
            startUnderlineOffsetPixels += advances[i]
        }
        var endUnderlineOffsetPixels = 0f
        for (i in text.lastIndex downTo lastUnderlineIndex + 1) {
            endUnderlineOffsetPixels += advances[i]
        }
        textStartUnderlinedPositionPixels =
            horizontalOffset - textLength / 2 + startUnderlineOffsetPixels
        textEndUnderlinedPositionPixels =
            horizontalOffset + textLength / 2 - endUnderlineOffsetPixels

    }

    // TODO image click doesn't work at all with scroll view
    private fun imageClicked(): Boolean {
        return getDistance(horizontalOffset, verticalOffset, event.x, event.y) <= imageRadius
    }

    override fun performClick(): Boolean {
        if (imageClicked() && performOnPictureClick()) return true
        return super.performClick()
    }

    override fun performLongClick(): Boolean {
        if (imageClicked() && performOnPictureLongClick()) return true
        return super.performLongClick()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        this.event = event
        return super.onTouchEvent(event)
    }

    private fun performOnPictureClick(): Boolean {
        return onPictureClickListeners?.invoke() ?: false
    }

    private fun performOnPictureLongClick(): Boolean {
        return onPictureLongClickListeners?.invoke() ?: false
    }

    fun setOnPictureClickListener(listener: () -> Boolean) {
        imageClickable = true
        onPictureClickListeners = (listener)
    }

    fun setOnPictureLongClickListener(listener: () -> Boolean) {
        imageLongClickable = true
        onPictureLongClickListeners = (listener)
    }

    fun setUnderline(start: Int, end: Int) {
        if (start < 0 || start > end || start > text.lastIndex || end > text.lastIndex) throw java.lang.IllegalArgumentException()
        firstUnderlineIndex = start
        lastUnderlineIndex = end
        setUnderlineMeasure()
        isUnderlined = true
        invalidate()
    }
}
