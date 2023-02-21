package ru.kekulta.goodjobray.screens.home.presentation

import android.graphics.Bitmap
import ru.kekulta.goodjobray.shared.data.models.User

data class HomeScreenState(
    val user: User?,
    val photo: Bitmap?,
    val progression: Int?,
    val tasksNumber: Int?
)