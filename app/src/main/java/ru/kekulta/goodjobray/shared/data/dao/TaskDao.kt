package ru.kekulta.goodjobray.activity.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(task: Task)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(task: List<Task>)

    @Update
    fun update(task: Task)

    @Update
    fun update(task: List<Task>)

    @Delete
    fun delete(task: Task)

    @Delete
    fun delete(tasks: List<Task>)

    @Query(
        """
        SELECT * 
        FROM ${Task.TASKS}
        """
    )
    fun getAllTasksLiveData(): LiveData<List<Task>>

    @Query(
        """
        SELECT * 
        FROM ${Task.TASKS} 
        WHERE ${Task.MONTH} = :numOfMonth
        """
    )
    fun getTasksForMonthLiveData(numOfMonth: Int): LiveData<List<Task>>

    @Query(
        """
        SELECT *
        FROM ${Task.TASKS}
        WHERE ${Task.DAY} = :day AND ${Task.MONTH} = :month AND ${Task.YEAR} = :year
        """
    )
    fun getTasksForDayLiveData(day: Int, month: Int, year: Int): LiveData<List<Task>>

    @Query(
        """
        SELECT *
        FROM ${Task.TASKS}
        WHERE ${Task.DAY} = :day AND ${Task.MONTH} = :month AND ${Task.YEAR} = :year
        """
    )
    fun getTasksForDay(day: Int, month: Int, year: Int): List<Task>

    @Query(
        """
        SELECT COUNT(${Task.ID})  FROM ${Task.TASKS}
        WHERE ${Task.DAY} = :day AND ${Task.MONTH} = :month AND ${Task.YEAR} = :year
        """
    )
    fun countTasksForDay(day: Int, month: Int, year: Int): Int

    @Query(
        """
        SELECT COUNT(${Task.ID})  FROM ${Task.TASKS}
        WHERE ${Task.DAY} = :day AND ${Task.MONTH} = :month AND ${Task.YEAR} = :year
        """
    )
    fun countTasksForDayLiveData(day: Int, month: Int, year: Int): LiveData<Int>
}