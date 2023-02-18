package ru.kekulta.goodjobray.activity.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(note: Note)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(note: List<Note>)

    @Update
    fun update(note: Note)

    @Update
    fun update(note: List<Note>)

    @Delete
    fun delete(note: Note)

    @Delete
    fun delete(note: List<Note>)

    @Query(
        """
        SELECT *
        FROM ${Note.NOTES} 
        WHERE ${Note.IS_PINNED} = 0
        """
    )
    fun getNotesLiveData(): LiveData<List<Note>>

    @Query(
        """
        SELECT * 
        FROM ${Note.NOTES} 
        WHERE ${Note.IS_PINNED} = 1
        """
    )
    fun getPinnedNotesLiveData(): LiveData<List<Note>>
}