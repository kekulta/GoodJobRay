package ru.kekulta.goodjobray.di

import android.content.Context
import androidx.fragment.app.FragmentManager
import ru.kekulta.goodjobray.activity.data.AppDatabase
import ru.kekulta.goodjobray.screens.home.data.UserRepository
import ru.kekulta.goodjobray.screens.main.navigator.MainNavigator
import ru.kekulta.goodjobray.screens.notes.data.NoteRepository
import ru.kekulta.goodjobray.screens.planner.data.TaskRepository

// FIXME Круто, что сделал свой DI
object DI {

    private var applicationContext: Context? = null

    private var appDatabase: AppDatabase? = null
    private var userRepository: UserRepository? = null
    private var taskRepository: TaskRepository? = null
    private var noteRepository: NoteRepository? = null
    private var navigator: MainNavigator? = null

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
            // TODO (19) Я лично считаю, что `!!` - это не очень хорошо, потому что нет внятного сообщения об ошибке.
            //   Куда лучше сделать `requireNotNull` и описать, что конкретно не так. Но для "ручного DI" - сойдёт
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

    fun initNavigator(fragmentManager: FragmentManager, container: Int, startScreen: String) {
        navigator = MainNavigator(fragmentManager, container, startScreen)
    }

    fun getNavigator(): MainNavigator {
        return requireNotNull(navigator) { "Navigator should be initialized" }
    }
}
