package ru.kekulta.goodjobray.screens.planner.data


import androidx.lifecycle.LiveData
import ru.kekulta.goodjobray.activity.data.Task
import ru.kekulta.goodjobray.activity.data.TaskDao
import ru.kekulta.goodjobray.utils.Date


class TaskRepository(private val dao: TaskDao) {

    fun observeTasksCountForDay(date: Date): LiveData<Int> {
        return dao.countTasksForDayLiveData(date.dayOfMonth, date.month, date.year)
    }

    fun getTasksCountForDay(date: Date): Int {
        return dao.countTasksForDay(date.dayOfMonth, date.month, date.year)
    }

    fun observeTasksForDay(date: Date): LiveData<List<Task>> {
        return dao.getTasksForDayLiveData(date.dayOfMonth, date.month, date.year)
    }

    fun getTasksForDay(date: Date): List<Task> {
        return dao.getTasksForDay(date.dayOfMonth, date.month, date.year)
    }

    fun deleteTask(task: Task) {
        dao.delete(task)
    }

    fun addTask(task: Task) {
        dao.insert(task)
    }
}