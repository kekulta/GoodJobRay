package ru.kekulta.goodjobray.activity.data

import android.content.Context
import androidx.room.*
import ru.kekulta.goodjobray.shared.data.dao.UserDao
import ru.kekulta.goodjobray.shared.data.models.User

// TODO (2) Никогда не оставляй на откуп каким-то библиотекам понимание того, как именно должны называться твои таблицы / поля и т.п.
//  У Room-а в аннотации Entity есть возможность указать название таблицы, индексы, foreign-ключи, если это нужно
//  Аналогично - про колонки, у каждого поля надо указать.

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
