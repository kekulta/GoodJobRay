package ru.kekulta.goodjobray.utils

import android.content.res.Resources
import android.util.TypedValue

data class Day(val dayOfMonth: Int, val monthNum: Int, val dayOfWeek: Int, val year: Int = 2022)

val Number.dp get() = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    Resources.getSystem().displayMetrics)
