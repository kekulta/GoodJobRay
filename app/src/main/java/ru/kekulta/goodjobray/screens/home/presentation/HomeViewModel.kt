package ru.kekulta.goodjobray.screens.home.presentation

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import ru.kekulta.goodjobray.App
import ru.kekulta.goodjobray.shared.data.models.User
import ru.kekulta.goodjobray.di.DI
import ru.kekulta.goodjobray.utils.Date
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import ru.kekulta.goodjobray.screens.main.navigator.MainNavigator
import ru.kekulta.goodjobray.screens.planner.data.TaskRepository
import ru.kekulta.goodjobray.shared.data.manager.UserManager
import java.io.InputStream

import kotlin.concurrent.thread


class HomeViewModel(
    private val userManager: UserManager,
    private val taskRepository: TaskRepository,
    private val internalSaver: InternalSaver
) : ViewModel() {
    private val handler = Handler(Looper.getMainLooper())

    private val tasks: LiveData<Int> = taskRepository.observeTasksCountForDay(Date.today())
    private val user: LiveData<User> = userManager.observeUser()
    private val progression = MutableLiveData(71)
    private val photo = MutableLiveData<Bitmap?>(null)

    private val state: MediatorLiveData<HomeScreenState> = MediatorLiveData()


    init {
        state.addSource(user) { newUser ->
            if (state.value?.user?.id != newUser.id) {
                loadPhotoFromInternalAsync()
            }
            // Порядок загрузки фото может измениться?
            state.value = HomeScreenState(
                newUser,
                state.value?.photo,
                state.value?.progression,
                state.value?.tasksNumber
            )
        }

        state.addSource(photo) { photo ->
            state.value = HomeScreenState(
                state.value?.user,
                photo,
                state.value?.progression,
                state.value?.tasksNumber
            )
        }

        state.addSource(progression) { progression ->
            state.value = HomeScreenState(
                state.value?.user,
                state.value?.photo,
                progression,
                state.value?.tasksNumber
            )
        }

        state.addSource(tasks) { tasksNum ->
            state.value = HomeScreenState(
                state.value?.user,
                state.value?.photo,
                state.value?.progression,
                tasksNum
            )
        }
    }

    private fun loadPhotoFromInternalAsync() {
        thread {
            Log.d(LOG_TAG, "Start async loading of user photo")
            try {
                val f = File(userManager.getUser()?.photo, "ProfilePicture.png")
                Log.d(LOG_TAG, "Bitmap from ${f.absolutePath} is loading")
                val b = BitmapFactory.decodeStream(FileInputStream(f))
                handler.post { photo.value = b }
                Log.d(LOG_TAG, "Bitmap from ${f.absolutePath} loaded")
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }
    }

    fun observeState(): LiveData<HomeScreenState> = state

    fun onHabitsButtonClicked() {
        App.INSTANCE.getNavigator().navigateTo(MainNavigator.HABITS)
    }

    fun setName(name: String) {
        userManager.getUser().let { user ->
            userManager.updateUser(User(user.id, name, user.photo))
        }
    }

    fun loadPhoto(stream: InputStream, uriFrom: String) {
        val user = userManager.getUser()
        stream.use {
            photo.value = BitmapFactory.decodeStream(it)
        }
        Log.d(
            LOG_TAG,
            "user: ${user}, bitmap: ${photo.value}"
        )
        thread {
            photo.value?.let { bitmap ->
                Log.d(LOG_TAG, "bitmap $bitmap is saving to the memory")
                val uriTo = internalSaver.saveToInternal(bitmap, "ProfilePicture")
                Log.d(LOG_TAG, "bitmap $bitmap saved to $uriTo")
                user.let { user ->
                    userManager.updateUser(User(user.id, user.name, uriTo))
                }
            }
        }


    }

    companion object {
        const val LOG_TAG = "HomeViewModel"

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                return HomeViewModel(
                    DI.getUserManager(), DI.getTaskRepository(), DI.getInternalSaver()
                ) as T
            }
        }
    }
}
