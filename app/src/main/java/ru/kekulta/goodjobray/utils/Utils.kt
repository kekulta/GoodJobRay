package ru.kekulta.goodjobray.utils

import android.content.res.Resources
import android.util.TypedValue


const val ID: Int = 123456

val Number.dp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )
