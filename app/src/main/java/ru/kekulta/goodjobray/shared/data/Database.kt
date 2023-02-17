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
