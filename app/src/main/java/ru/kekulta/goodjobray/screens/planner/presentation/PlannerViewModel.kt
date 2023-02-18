package ru.kekulta.goodjobray.screens.planner.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import ru.kekulta.goodjobray.di.DI
import ru.kekulta.goodjobray.activity.data.Task
import ru.kekulta.goodjobray.utils.Date

class PlannerViewModel : ViewModel() {


    //private val model = PlannerModel(app)

    private val taskRepository = DI.getTaskRepository()

    private val _currentDate = MutableLiveData(Date.today())
    val currentDate: LiveData<Date>
        get() = _currentDate

    val tasks: LiveData<List<Task>>
        get() = currentDate.switchMap { day ->
            taskRepository.observeTasksForDay(day)
        }

    val days: LiveData<List<Date>>
        get() = currentDate.map { day -> Date.getDaysFor(day.month, day.year) }

    private fun deleteTask(task: Task) {
        taskRepository.deleteTask(task)
    }


    private fun setCurrentDay(date: Date) {
        _currentDate.value = date
    }

    private fun switchToNextMonth() {
        val nextMonth = ((currentDate.value?.month ?: 0) + 1) % 12
        val nextYear =
            (currentDate.value?.year ?: Date.actualYear) + ((currentDate.value?.month
                ?: Date.actualMonth) + 1) / 12
        //FIXME создаём массив только чтобы узнать длину
        val nextDays = Date.getDaysFor(nextMonth, nextYear)
        val nextDay =
            if ((currentDate.value?.dayOfMonth ?: Date.actualDayOfMonth) > nextDays.size
            ) nextDays.size else (currentDate.value?.dayOfMonth ?: Date.actualDayOfMonth)

        _currentDate.value = Date(nextDay, nextMonth, nextYear)
    }

    fun addTask(hourFrom: Int, hourTo: Int = hourFrom, title: String?) {
        val task = Task(
            id = System.currentTimeMillis().toInt(),
            title = title
                ?: "Task ${Date.actualHour}-${Date.actualDayOfMonth}-${Date.actualMonth}-${Date.actualYear}",
            month = currentDate.value?.month ?: Date.actualMonth,
            dayOfMonth = currentDate.value?.dayOfMonth ?: Date.actualDayOfMonth,
            year = currentDate.value?.year ?: Date.actualYear,
            hourFrom,
            hourTo

        )
        taskRepository.addTask(task)
    }

    private fun switchToPreviousMonth() {
        val previousMonth =
            if ((currentDate.value?.month
                    ?: Date.actualMonth) - 1 < 0
            ) 11 else (currentDate.value?.month
                ?: Date.actualMonth) - 1
        val previousYear = if (previousMonth == 11) (currentDate.value?.year
            ?: Date.actualYear) - 1 else (currentDate.value?.year ?: Date.actualYear)
        // FIXME создаём массив только чтобы узнать длину
        val previousDays = Date.getDaysFor(previousMonth, previousYear)
        val previousDay = if ((currentDate.value?.dayOfMonth
                ?: Date.actualDayOfMonth) > previousDays.size
        ) previousDays.size else (currentDate.value?.dayOfMonth ?: Date.actualDayOfMonth)

        _currentDate.value = Date(previousDay, previousMonth, previousYear)
    }

    fun dayClicked(day: Int) {
        _currentDate.value = _currentDate.value?.setDay(day)
    }

    fun nextMonthButtonClicked() {
        switchToNextMonth()
    }

    fun previousMonthButtonClicked() {
        switchToPreviousMonth()
    }

    fun taskDeleteButtonClicked(task: Task) {
        taskRepository.deleteTask(task)
    }
}