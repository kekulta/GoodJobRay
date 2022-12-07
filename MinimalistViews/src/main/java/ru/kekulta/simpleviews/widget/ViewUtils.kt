package ru.kekulta.simpleviews.widget

import android.graphics.*
import java.lang.Math.toDegrees
import kotlin.math.pow
import kotlin.math.acos
import kotlin.math.min


internal fun getCroppedBitmap(bitmap: Bitmap): Bitmap {

    val size = min(bitmap.width, bitmap.height)
    val croppedBitmap = Bitmap.createBitmap(bitmap, (bitmap.width - size) / 2, (bitmap.height - size) / 2, size, size)

    val output = Bitmap.createBitmap(
        croppedBitmap.width,
        croppedBitmap.height, Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(output)
    val color = -0xbdbdbe
    val paint = Paint()
    val rect = Rect(0, 0, croppedBitmap.width, croppedBitmap.height)
    paint.isAntiAlias = true
    canvas.drawARGB(0, 0, 0, 0)
    paint.color = color
    canvas.drawCircle(
        croppedBitmap.width / 2f, croppedBitmap.height / 2f,
        croppedBitmap.width / 2f, paint
    )
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(croppedBitmap, rect, rect, paint)
    return output
}

internal fun getDistance(x0: Float, y0: Float, x1: Float, y1: Float): Float {
    return ((x0 - x1).pow(2) + (y0 - y1).pow(2)).pow(0.5f)
}

internal fun dayOfWeek(dayNum: Int): String {
    return when (dayNum) {
        0 -> "Mon"
        1 -> "Tue"
        2 -> "Wen"
        3 -> "Thu"
        4 -> "Fri"
        5 -> "Sat"
        6 -> "Sun"
        else -> throw java.lang.IllegalArgumentException()
    }
}


internal fun crossProductZ(x0: Float, y0: Float, x1: Float, y1: Float): Float {
    return x0 * y1 - y0 * x1
}


internal fun angleBetweenTwoVectors(
    x0: Float,
    y0: Float,
    x1: Float,
    y1: Float,
    startX: Float = 0f,
    startY: Float = 0f
): Float {

    val degrees = toDegrees(
        (acos(
            (x0 - startX) / getDistance(startX, startY, x0, y0) *
                    (x1 - startX) / getDistance(startX, startY, x1, y1) +
                    (y0 - startY) / getDistance(startX, startY, x0, y0) *
                    (y1 - startY) / getDistance(startX, startY, x1, y1)
        )).toDouble()
    ).toFloat()

    val crossProduct = crossProductZ(
        (x0 - startX) / getDistance(startX, startY, x0, y0),
        (y0 - startY) / getDistance(startX, startY, x0, y0),
        (x1 - startX) / getDistance(startX, startY, x1, y1),
        (y1 - startY) / getDistance(startX, startY, x1, y1)
    )
    return if (crossProduct < 0) -degrees
    else degrees
}

