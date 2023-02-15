package ru.kekulta.goodjobray.activity.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import ru.kekulta.goodjobray.utils.Day

import kotlin.random.Random

// TODO (1) -> гораздо лучше, если каждому классу будет соответствовать свой файл - удобнее смотреть и искать отдельные классы

// TODO (2) Никогда не оставляй на откуп каким-то библиотекам понимание того, как именно должны называться твои таблицы / поля и т.п.
//  У Room-а в аннотации Entity есть возможность указать название таблицы, индексы, foreign-ключи, если это нужно
//  Аналогично - про колонки, у каждого поля надо указать.

// TODO (3) Очень непонятно, почему база данных и её классы лежат в пакете .activity . `.activity` - это название фичи?
//  Или имелась в виду андроидная Activity? Гораздо понятнее, когда база данных лежит ближе к слою `Data` по Clean-у.
//  Можно перенести в пакет .data просто, без `.activity`

// TODO (6) Я б разнёс классы-модели и классы-DAO по пакетам --> .entity (или .model, или .models, whatever) / .dao

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
    // TODO (4) Обычно синтаксис SQL-я пишут с большой буквы, просто такой принятый codestyle
    //  SELECT * FROM ...
    // TODO Когда пишешь SQL-выражения для Room очень удобно пользоваться фичой котлина - multiline string
    // @Query("""
    //        SELECT *
    //        FROM User
    //        WHERE id = :id
    //    """)
    // TODO (5) Вынеси константы названий колонок и таблиц в объекты и используй в SQL-выражениях именно их.
    //   Это поможет разом поменять какие-то названия если что и не сломать при этом SQL-выражения.
    /*

    object TablesNames {
        const val USER = "User"
        const val TASK = "Task"
    }

     */
    @Query("""
        select * 
        from User 
        where id = :id
    """)
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
    fun getNotes(): List<Note>

    @Query("select * from Note where pinned = 1")
    fun getPinnedNotes(): List<Note>
}


// TODO (7) А почему exportSchema = false ?
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
