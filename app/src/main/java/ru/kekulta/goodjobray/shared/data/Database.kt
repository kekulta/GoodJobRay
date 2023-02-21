package ru.kekulta.goodjobray.activity.data

import android.content.Context
import androidx.room.*
import ru.kekulta.goodjobray.shared.data.dao.UserDao
import ru.kekulta.goodjobray.shared.data.models.User


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
