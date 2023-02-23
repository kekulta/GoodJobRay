package ru.kekulta.goodjobray.screens.home.presentation

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.kekulta.goodjobray.App
import ru.kekulta.goodjobray.shared.data.models.User
import ru.kekulta.goodjobray.di.DI
import ru.kekulta.goodjobray.utils.Date
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import ru.kekulta.goodjobray.screens.main.navigator.MainNavigator
import ru.kekulta.goodjobray.screens.planner.data.TaskRepository
import ru.kekulta.goodjobray.shared.data.User.UserManager
import java.io.InputStream

import kotlin.concurrent.thread


class HomeViewModel(
    private val userManager: UserManager,
    private val taskRepository: TaskRepository,
    private val internalSaver: InternalSaver
) : ViewModel() {
    private val tasks: LiveData<Int> =
        taskRepository.observeTasksCountForDay(Date.today())
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
            state.value = state.value?.copy(user = newUser) ?: HomeScreenState(user = newUser)
        }

        state.addSource(photo) { photo ->
            state.value = state.value?.copy(photo = photo) ?: HomeScreenState(photo = photo)
        }

        state.addSource(progression) { progression ->
            state.value = state.value?.copy(progression = progression) ?: HomeScreenState(
                progression = progression
            )
        }

        state.addSource(tasks) { tasksNum ->
            state.value =
                state.value?.copy(tasksNumber = tasksNum) ?: HomeScreenState(tasksNumber = tasksNum)
        }
    }

    private fun loadPhotoFromInternalAsync() {
        thread {
            Log.d(LOG_TAG, "Start async loading of user photo")
            try {
                val f = File(userManager.getUser().photo, "ProfilePicture.png")
                Log.d(LOG_TAG, "Bitmap from ${f.absolutePath} is loading")
                val b = BitmapFactory.decodeStream(FileInputStream(f))
                photo.postValue(b)
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
                    userManager.updateUser(user.copy(photo = uriTo))
                }
            }
        }


    }

    companion object {
        const val LOG_TAG = "HomeViewModel"

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                HomeViewModel(
                    DI.getUserManager(), DI.getTaskRepository(), DI.getInternalSaver()
                )
            }
        }
    }
}
