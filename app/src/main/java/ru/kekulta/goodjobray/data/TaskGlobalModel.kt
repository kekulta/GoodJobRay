package ru.kekulta.goodjobray.data


import ru.kekulta.goodjobray.utils.ModelAccessInterfaces.DataObservable
import ru.kekulta.goodjobray.utils.ModelAccessInterfaces.TaskModel


object TaskGlobalModel : TaskModel, DataObservable {


    private val removedTasks = mutableListOf<Task>()

    private val observers: MutableList<() -> Unit> = mutableListOf()


    override fun notifyObservers() {
        observers.let { listeners ->
            listeners.forEach { listener -> listener.invoke() }
        }
    }

    private var _taskList: MutableList<Task> = mutableListOf()
    val taskList: List<Task> get(): List<Task> = _taskList


    override fun getTasksFor(day: Int?, month: Int?, year: Int?): List<Task> {
        println("request for tasks at $day-$month-$year in GlobalModel")
        if (day == null || month == null || year == null) return listOf()
        return taskList.filter { it.dayOfMonth == day && it.numOfMonth == month && it.year == year }
    }



    override fun deleteTask(task: Task) {
        _taskList.remove(task)
        removedTasks.add(task)
        notifyObservers()
    }

    override fun addTask(task: Task) {
        _taskList.add(task)
        notifyObservers()
    }

    override fun saveTaskChanges(dao: TaskDao) {
        println("Start saving changes from GlobalModel")
        dao.delete(removedTasks)
        dao.insert(taskList)
        println("Changes Saved")
    }

    override fun loadTasks(dao: TaskDao) {
        _taskList.addAll(dao.getAllTasks())
        notifyObservers()
    }

    override fun addObserver(observer: () -> Unit) {
        observers.add(observer)
    }

    override fun removeObserver(observer: () -> Unit) {
        observers.remove(observer)
    }

}