package ru.kekulta.goodjobray.shared.data.utils

import android.content.res.Resources
import android.util.TypedValue
import java.time.LocalDate


val Number.dp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )

