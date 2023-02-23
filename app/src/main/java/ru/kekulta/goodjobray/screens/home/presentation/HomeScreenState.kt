package ru.kekulta.goodjobray.screens.home.presentation

import android.graphics.Bitmap
import ru.kekulta.goodjobray.shared.data.models.User

data class HomeScreenState(
    val user: User? = null,
    val photo: Bitmap? = null,
    val progression: Int? = null,
    val tasksNumber: Int? = null
)