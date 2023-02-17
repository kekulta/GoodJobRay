package ru.kekulta.goodjobray.activity.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(task: Task)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(task: List<Task>)

    @Delete
    fun delete(task: Task)

    @Delete
    fun delete(tasks: List<Task>)

    @Query("select * from Task")
    fun getAllTasks(): List<Task>

    @Query("select * from Task where numOfMonth = :numOfMonth")
    fun getTasksForMonth(numOfMonth: Int): List<Task>

    @Query("select * from Task where dayOfMonth = :day and numOfMonth = :month and year = :year")
    fun getTasksForDay(day: Int, month: Int, year: Int): List<Task>

}