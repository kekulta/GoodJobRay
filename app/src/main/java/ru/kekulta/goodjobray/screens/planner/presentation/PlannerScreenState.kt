package ru.kekulta.goodjobray.screens.planner.presentation

import ru.kekulta.goodjobray.activity.data.Task
import ru.kekulta.goodjobray.utils.Date

data class PlannerScreenState(
    val currentDate: Date?,
    val days: List<Date>?,
    val tasks: List<Task>?,
)