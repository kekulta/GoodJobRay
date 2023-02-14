package ru.kekulta.goodjobray.screens.planner.data


import ru.kekulta.goodjobray.activity.data.Task
import ru.kekulta.goodjobray.activity.data.TaskDao
import ru.kekulta.goodjobray.utils.DataObservable


class TaskRepository(private val dao: TaskDao) : DataObservable {


    private val observers: MutableList<() -> Unit> = mutableListOf()


    init {
        notifyObservers()
    }

    fun getTasksForDay(day: Int?, month: Int?, year: Int?): List<Task> {
        println("request for tasks at $day-$month-$year in GlobalModel")
        if (day == null || month == null || year == null) return listOf()
        return dao.getTasksForDay(day, month, year)
    }


    fun deleteTask(task: Task) {
        dao.delete(task)
        notifyObservers()
    }

    fun addTask(task: Task) {
        dao.insert(task)
        notifyObservers()
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