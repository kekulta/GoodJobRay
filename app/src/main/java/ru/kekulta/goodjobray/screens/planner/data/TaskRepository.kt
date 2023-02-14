package ru.kekulta.goodjobray.screens.planner.data


import ru.kekulta.goodjobray.activity.data.Task
import ru.kekulta.goodjobray.activity.data.TaskDao
import ru.kekulta.goodjobray.utils.DataObservable


class TaskRepository(private val dao: TaskDao) : DataObservable {

    private val removedTasks = mutableListOf<Task>()

    private val observers: MutableList<() -> Unit> = mutableListOf()

    private var _taskList: MutableList<Task> = mutableListOf()
    private val taskList: List<Task> get(): List<Task> = _taskList

    init {
        _taskList.addAll(dao.getAllTasks())
        notifyObservers()
    }

    fun getTasksFor(day: Int?, month: Int?, year: Int?): List<Task> {
        println("request for tasks at $day-$month-$year in GlobalModel")
        if (day == null || month == null || year == null) return listOf()
        return taskList.filter { it.dayOfMonth == day && it.numOfMonth == month && it.year == year }
    }


    fun deleteTask(task: Task) {
        _taskList.remove(task)
        removedTasks.add(task)
        notifyObservers()
    }

    fun addTask(task: Task) {
        _taskList.add(task)
        notifyObservers()
    }

    fun saveTaskChanges() {
        println("Start saving changes from GlobalModel")
        dao.delete(removedTasks)
        dao.insert(taskList)
        println("Changes Saved")
    }


    override fun notifyObservers() {
        observers.let { listeners ->
            listeners.forEach { listener -> listener.invoke() }
        }
    }

    override fun addObserver(observer: () -> Unit) {
        observers.add(observer)
    }

    override fun removeObserver(observer: () -> Unit) {
        observers.remove(observer)
    }

}