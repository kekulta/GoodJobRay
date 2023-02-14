package ru.kekulta.goodjobray.di

import android.content.Context
import ru.kekulta.goodjobray.activity.data.AppDatabase
import ru.kekulta.goodjobray.activity.presentation.Navigator
import ru.kekulta.goodjobray.screens.home.data.UserRepository
import ru.kekulta.goodjobray.screens.notes.data.NoteRepository
import ru.kekulta.goodjobray.screens.planner.data.TaskRepository

object DI {

    private var applicationContext: Context? = null

    private var appDatabase: AppDatabase? = null
    private var userRepository: UserRepository? = null
    private var taskRepository: TaskRepository? = null
    private var noteRepository: NoteRepository? = null
    private var navigator: Navigator? = null

    fun initDi(applicationContext: Context) {
        DI.applicationContext = applicationContext
    }

    private fun getAppDatabase(): AppDatabase {
        applicationContext.let { context ->
            requireNotNull(context) { "DI should be initialized before accessing AppDatabase" }
            if (appDatabase == null) {
                AppDatabase.initDatabase(context)
                appDatabase = AppDatabase.getDatabase()
            }
            // Можно ли сделать без !!
            return appDatabase!!
        }
    }

    fun getUserRepository(): UserRepository {
        if (userRepository == null) {
            userRepository = UserRepository(getAppDatabase().getUserDao())
        }
        return userRepository!!
    }

    fun getTaskRepository(): TaskRepository {
        if (taskRepository == null) {
            taskRepository = TaskRepository(getAppDatabase().getTaskDao())
        }
        return taskRepository!!
    }

    fun getNoteRepository(): NoteRepository {
        if (noteRepository == null) {
            noteRepository = NoteRepository(getAppDatabase().getNoteDao())
        }
        return noteRepository!!
    }

    fun getNavigator(): Navigator {
        if (navigator == null) {
            navigator = Navigator()
        }
        return navigator!!
    }
}