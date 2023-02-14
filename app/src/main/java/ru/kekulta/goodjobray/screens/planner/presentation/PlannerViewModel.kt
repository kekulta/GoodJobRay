package ru.kekulta.goodjobray.screens.planner.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.kekulta.goodjobray.di.DI
import ru.kekulta.goodjobray.activity.data.Task
import ru.kekulta.goodjobray.utils.Day

class PlannerViewModel() : ViewModel() {


    //private val model = PlannerModel(app)

   private val taskRepository = DI.getTaskRepository()

    private val _currentHour = MutableLiveData(Day.actualHour)
    val currentHour get() = _currentHour

    private val _currentDay = MutableLiveData(Day.actualDay)
    val currentDay: LiveData<Int>
        get() = _currentDay

    private val _currentMonth = MutableLiveData(Day.actualMonth)
    val currentMonth: LiveData<Int>
        get() = _currentMonth

    private val _currentYear = MutableLiveData(Day.actualYear)
    val currentYear: LiveData<Int>
        get() = _currentYear

    private val _tasks =
        MutableLiveData(taskRepository.getTasksForDay(Day.actualDay, Day.actualMonth, Day.actualYear))
    val tasks: LiveData<List<Task>>
        get() = _tasks

    private val _days = MutableLiveData(Day.getDaysFor(Day.actualMonth, Day.actualYear))
    val days: LiveData<List<Day>>
        get() = _days

    fun deleteTask(task: Task) {
        taskRepository.deleteTask(task)
        //updateTasks()
    }

    init {
        taskRepository.addObserver {
            updateTasks()
        }
    }

    private fun updateTasks() {
        _tasks.value = taskRepository.getTasksForDay(
            currentDay.value ?: Day.actualDay,
            currentMonth.value ?: Day.actualMonth,
            currentYear.value ?: Day.actualYear
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
        _tasks.value = taskRepository.getTasksForDay(day, month, year)
    }

    fun nextMonth() {
        val nextMonth = ((currentMonth.value ?: 0) + 1) % 12
        val nextYear =
            (currentYear.value ?: Day.actualYear) + ((currentMonth.value
                ?: Day.actualMonth) + 1) / 12
        val nextDays = Day.getDaysFor(nextMonth, nextYear)
        val nextDay = if ((currentDay.value
                ?: Day.actualDay) > nextDays.last().dayOfMonth
        ) nextDays.last().dayOfMonth else (currentDay.value ?: Day.actualDay)

        _currentYear.value = nextYear
        _currentMonth.value = nextMonth
        _currentDay.value = nextDay
        _days.value = nextDays
        _tasks.value = taskRepository.getTasksForDay(nextDay, nextMonth, nextYear)

    }

    fun addTask(hourFrom: Int, hourTo: Int = hourFrom, title: String?) {
        val task = Task(
            id = System.currentTimeMillis().toInt(),
            title = title
                ?: "Task ${currentHour.value}-${currentDay.value}-${currentMonth.value}-${currentYear.value}",
            numOfMonth = currentMonth.value ?: Day.actualMonth,
            dayOfMonth = currentDay.value ?: Day.actualDay,
            year = currentYear.value ?: Day.actualYear,
            hourFrom,
            hourTo

        )
        taskRepository.addTask(task)
        //updateTasks()
    }

    fun previousMonth() {
        val previousMonth =
            if ((currentMonth.value ?: Day.actualMonth) - 1 < 0) 11 else (currentMonth.value
                ?: Day.actualMonth) - 1
        val previousYear = if (previousMonth == 11) (currentYear.value
            ?: Day.actualYear) - 1 else (currentYear.value ?: Day.actualYear)
        val previousDays = Day.getDaysFor(previousMonth, previousYear)
        val previousDay = if ((currentDay.value
                ?: Day.actualDay) > previousDays.last().dayOfMonth
        ) previousDays.last().dayOfMonth else (currentDay.value ?: Day.actualDay)


        _currentYear.value = previousYear
        _currentMonth.value = previousMonth
        _currentDay.value = previousDay
        _days.value = previousDays
        _tasks.value = taskRepository.getTasksForDay(previousDay, previousMonth, previousYear)
    }

}