package ru.kekulta.goodjobray.activity.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: Note)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: List<Note>)

    @Delete
    fun delete(note: Note)

    @Delete
    fun delete(note: List<Note>)

    @Query("select * from Note where pinned = 0")
    fun getNotPinnedNotesLiveData(): LiveData<List<Note>>

    @Query("select * from Note where pinned = 1")
    fun getPinnedNotesLiveData(): LiveData<List<Note>>

    @Query("select * from Note where pinned = 0")
    fun getNotes(): List<Note>

    @Query("select * from Note where pinned = 1")
    fun getPinnedNotes(): List<Note>
}