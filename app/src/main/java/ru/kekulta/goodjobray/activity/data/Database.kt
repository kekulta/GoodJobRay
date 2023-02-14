package ru.kekulta.goodjobray.activity.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import ru.kekulta.goodjobray.utils.Day

import kotlin.random.Random

@Entity
data class User(
    @PrimaryKey val id: Int,
    val name: String,
    val photo: String? = null,
)

@Entity
data class Task(
    @PrimaryKey val id: Int = Random.nextInt(),
    val title: String = "Task#$id",
    val numOfMonth: Int = Day.actualMonth,
    val dayOfMonth: Int = Day.actualDay,
    val year: Int = Day.actualYear,
    val fromHour: Int,
    val toHour: Int = fromHour,
)

@Entity
data class Note(
    @PrimaryKey val id: Int,
    val title: String = "",
    val text: String = "",
    val pinned: Boolean = false,
    val time: Long = System.currentTimeMillis()
)


@Dao
interface UserDao {
    @Query("select * from User where id = :id")
    fun getUser(id: Int): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)
}

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
    fun getNotPinnedNotes(): List<Note>

    @Query("select * from Note where pinned = 1")
    fun getPinnedNotes(): List<Note>
}


@Database(entities = [Task::class, User::class, Note::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getTaskDao(): TaskDao
    abstract fun getUserDao(): UserDao
    abstract fun getNoteDao(): NoteDao

    companion object {
        private var instance: AppDatabase? = null

        fun initDatabase(context: Context) {
            if (instance == null) {
                instance = Room
                    .databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        "tasks.db"
                    )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }

        }

        fun getDatabase(): AppDatabase {
            return requireNotNull(instance) {
                "Database wasn't initialized!"
            }
        }
    }

}
