package ru.kekulta.goodjobray.di

import android.annotation.SuppressLint
import android.content.Context
import ru.kekulta.goodjobray.activity.data.AppDatabase
import ru.kekulta.goodjobray.shared.data.User.UserRepository
import ru.kekulta.goodjobray.screens.home.presentation.InternalSaver
import ru.kekulta.goodjobray.screens.notes.data.NoteRepository
import ru.kekulta.goodjobray.screens.planner.data.TaskRepository
import ru.kekulta.goodjobray.shared.data.User.UserManager


object DI {

    private var applicationContext: Context? = null

    private var appDatabase: AppDatabase? = null
    private var userRepository: UserRepository? = null
    private var taskRepository: TaskRepository? = null
    private var noteRepository: NoteRepository? = null

    @SuppressLint("StaticFieldLeak")
    private var internalSaver: InternalSaver? = null
    private var userManager: UserManager? = null

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

            return appDatabase!!
        }
    }

    fun getInternalSaver(): InternalSaver {
        applicationContext.let { context ->
            requireNotNull(context) { "DI should be initialized before accessing InternalSaver" }
            if (internalSaver == null) {
                internalSaver = InternalSaver(context)
            }

            return internalSaver!!
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

    fun getUserManager(): UserManager {
        if (userManager == null) {
            userManager = UserManager(getUserRepository())
        }
        return userManager!!
    }
}
