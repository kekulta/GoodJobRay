package ru.kekulta.goodjobray.utils.ModelAccessInterfaces

import ru.kekulta.goodjobray.data.Task
import ru.kekulta.goodjobray.data.TaskDao

interface TaskModel{

    fun getTasksFor(day: Int?, month: Int?, year: Int?): List<Task>
    fun deleteTask(task: Task)
    fun addTask(task: Task)

    fun saveTaskChanges(dao: TaskDao)
    fun loadTasks(dao: TaskDao)
}