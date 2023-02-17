package ru.kekulta.goodjobray.activity.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.kekulta.goodjobray.utils.Day
import kotlin.random.Random

@Entity
data class Task(
    @PrimaryKey val id: Int = Random.nextInt(),
    val title: String = "Task#$id",
    val numOfMonth: Int = Day.actualMonth,
    val dayOfMonth: Int = Day.actualDay,
    val year: Int = Day.actualYear,
    val fromHour: Int,
    val toHour: Int = fromHour,
)