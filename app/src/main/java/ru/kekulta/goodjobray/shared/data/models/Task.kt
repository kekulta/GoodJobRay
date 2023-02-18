package ru.kekulta.goodjobray.activity.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.kekulta.goodjobray.utils.Date
import kotlin.random.Random

@Entity(
    tableName = Task.TASKS
)
data class Task(
    @ColumnInfo(name = ID)
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = TITLE)
    var title: String = "Task#${Random.nextInt()}",
    @ColumnInfo(name = MONTH)
    var month: Int = Date.actualMonth,
    @ColumnInfo(name = DAY)
    var dayOfMonth: Int = Date.actualDayOfMonth,
    @ColumnInfo(name = YEAR)
    var year: Int = Date.actualYear,
    @ColumnInfo(name = HOUR_FROM)
    var fromHour: Int,
    @ColumnInfo(name = HOUR_TO)
    var toHour: Int = fromHour,
) {


    companion object {
        const val TASKS = "tasks"
        const val ID = "tasks_id"
        const val TITLE = "tasks_title"
        const val MONTH = "tasks_month"
        const val DAY = "tasks_day"
        const val YEAR = "tasks_year"
        const val HOUR_FROM = "tasks_hour_from"
        const val HOUR_TO = "tasks_hour_to"
    }
}