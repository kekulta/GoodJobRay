package ru.kekulta.goodjobray.screens.planner.presentation

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewmodel.CreationExtras
import ru.kekulta.goodjobray.di.DI
import ru.kekulta.goodjobray.activity.data.Task
import ru.kekulta.goodjobray.screens.planner.data.TaskRepository
import ru.kekulta.goodjobray.utils.Date

class PlannerViewModel(private val taskRepository: TaskRepository) : ViewModel() {

    var tasksScrollState: Int? = null
    var datesRecyclerState: Parcelable? = null

    private val currentDate = MutableLiveData(Date.today())

    private val tasks: LiveData<List<Task>>
        get() = currentDate.switchMap { day ->
            taskRepository.observeTasksForDay(day)
        }

    private val days: LiveData<List<Date>>
        get() = currentDate.map { day -> Date.getDaysFor(day.month, day.year) }

    private val plannerScreenState = MediatorLiveData<PlannerScreenState>()

    init {
        plannerScreenState.addSource(currentDate) { date ->
            plannerScreenState.value = PlannerScreenState(
                date,
                plannerScreenState.value?.days,
                plannerScreenState.value?.tasks
            )
        }

        plannerScreenState.addSource(tasks) { tasks ->
            plannerScreenState.value = PlannerScreenState(
                plannerScreenState.value?.currentDate,
                plannerScreenState.value?.days,
                tasks
            )
        }

        plannerScreenState.addSource(days) { days ->
            plannerScreenState.value = PlannerScreenState(
                plannerScreenState.value?.currentDate,
                days,
                plannerScreenState.value?.tasks
            )
        }
    }

    fun observeState(): LiveData<PlannerScreenState> = plannerScreenState

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

    fun dayClicked(day: Int) {
        currentDate.value = currentDate.value?.setDay(day)
    }

    fun nextMonthButtonClicked() {
        val nextMonth = ((currentDate.value?.month ?: 0) + 1) % 12
        val nextYear =
            (currentDate.value?.year ?: Date.actualYear) + ((currentDate.value?.month
                ?: Date.actualMonth) + 1) / 12
        //FIXME создаём массив только чтобы узнать длину
        val nextDays = Date.getDaysFor(nextMonth, nextYear)
        val nextDay =
            if ((currentDate.value?.dayOfMonth ?: Date.actualDayOfMonth) > nextDays.size
            ) nextDays.size else (currentDate.value?.dayOfMonth ?: Date.actualDayOfMonth)

        currentDate.value = Date(nextDay, nextMonth, nextYear)
    }

    fun previousMonthButtonClicked() {
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

        currentDate.value = Date(previousDay, previousMonth, previousYear)
    }

    fun taskDeleteButtonClicked(task: Task) {
        taskRepository.deleteTask(task)
    }

    companion object {
        const val LOG_TAG = "PlannerViewModel"

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                return PlannerViewModel(
                    DI.getTaskRepository()
                ) as T
            }
        }
    }
}