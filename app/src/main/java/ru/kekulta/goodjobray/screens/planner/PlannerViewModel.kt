package ru.kekulta.goodjobray.screens.planner

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.kekulta.goodjobray.data.DateGlobalModel
import ru.kekulta.goodjobray.data.TaskGlobalModel
import ru.kekulta.goodjobray.data.Task
import ru.kekulta.goodjobray.utils.Day

class PlannerViewModel() : ViewModel() {


    //private val model = PlannerModel(app)

   private val taskModel = TaskGlobalModel
    private val dateModel = DateGlobalModel

    private val _currentHour = MutableLiveData(dateModel.actualHour)
    val currentHour get() = _currentHour

    private val _currentDay = MutableLiveData(dateModel.actualDay)
    val currentDay: LiveData<Int>
        get() = _currentDay

    private val _currentMonth = MutableLiveData(dateModel.actualMonth)
    val currentMonth: LiveData<Int>
        get() = _currentMonth

    private val _currentYear = MutableLiveData(dateModel.actualYear)
    val currentYear: LiveData<Int>
        get() = _currentYear

    private val _tasks =
        MutableLiveData(taskModel.getTasksFor(dateModel.actualDay, dateModel.actualMonth, dateModel.actualYear))
    val tasks: LiveData<List<Task>>
        get() = _tasks

    private val _days = MutableLiveData(dateModel.getDaysFor(dateModel.actualMonth, dateModel.actualYear))
    val days: LiveData<List<Day>>
        get() = _days

    fun deleteTask(task: Task) {
        taskModel.deleteTask(task)
        //updateTasks()
    }

    init {
        taskModel.addObserver {
            updateTasks()
        }
    }

    private fun updateTasks() {
        _tasks.value = taskModel.getTasksFor(
            currentDay.value ?: dateModel.actualDay,
            currentMonth.value ?: dateModel.actualMonth,
            currentYear.value ?: dateModel.actualYear
        )
    }


    fun setCurrentDay(
        day: Int? = currentDay.value,
        month: Int? = currentMonth.value,
        year: Int? = currentYear.value
    ) {
        _currentDay.value = day
        _currentMonth.value = month
        _currentYear.value = year
        _tasks.value = taskModel.getTasksFor(day, month, year)
    }

    fun nextMonth() {
        val nextMonth = ((currentMonth.value ?: 0) + 1) % 12
        val nextYear =
            (currentYear.value ?: dateModel.actualYear) + ((currentMonth.value
                ?: dateModel.actualMonth) + 1) / 12
        val nextDays = dateModel.getDaysFor(nextMonth, nextYear)
        val nextDay = if ((currentDay.value
                ?: dateModel.actualDay) > nextDays.last().dayOfMonth
        ) nextDays.last().dayOfMonth else (currentDay.value ?: dateModel.actualDay)

        _currentYear.value = nextYear
        _currentMonth.value = nextMonth
        _currentDay.value = nextDay
        _days.value = nextDays
        _tasks.value = taskModel.getTasksFor(nextDay, nextMonth, nextYear)

    }

    fun addTask(hourFrom: Int, hourTo: Int = hourFrom, title: String?) {
        val task = Task(
            id = System.currentTimeMillis().toInt(),
            title = title
                ?: "Task ${currentHour.value}-${currentDay.value}-${currentMonth.value}-${currentYear.value}",
            numOfMonth = currentMonth.value ?: dateModel.actualMonth,
            dayOfMonth = currentDay.value ?: dateModel.actualDay,
            year = currentYear.value ?: dateModel.actualYear,
            hourFrom,
            hourTo

        )
        taskModel.addTask(task)
        //updateTasks()
    }

    fun previousMonth() {
        val previousMonth =
            if ((currentMonth.value ?: dateModel.actualMonth) - 1 < 0) 11 else (currentMonth.value
                ?: dateModel.actualMonth) - 1
        val previousYear = if (previousMonth == 11) (currentYear.value
            ?: dateModel.actualYear) - 1 else (currentYear.value ?: dateModel.actualYear)
        val previousDays = dateModel.getDaysFor(previousMonth, previousYear)
        val previousDay = if ((currentDay.value
                ?: dateModel.actualDay) > previousDays.last().dayOfMonth
        ) previousDays.last().dayOfMonth else (currentDay.value ?: dateModel.actualDay)


        _currentYear.value = previousYear
        _currentMonth.value = previousMonth
        _currentDay.value = previousDay
        _days.value = previousDays
        _tasks.value = taskModel.getTasksFor(previousDay, previousMonth, previousYear)
    }

}