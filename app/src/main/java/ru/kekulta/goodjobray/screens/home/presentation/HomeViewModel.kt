package ru.kekulta.goodjobray.screens.home.presentation

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import ru.kekulta.goodjobray.shared.data.models.User
import ru.kekulta.goodjobray.di.DI
import ru.kekulta.goodjobray.utils.Date
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import ru.kekulta.goodjobray.screens.home.data.ID
import ru.kekulta.goodjobray.screens.home.data.UserRepository
import ru.kekulta.goodjobray.screens.main.navigator.MainNavigator
import ru.kekulta.goodjobray.screens.planner.data.TaskRepository
import java.io.InputStream

import kotlin.concurrent.thread


class HomeViewModel(
    private val userRepository: UserRepository,
    private val taskRepository: TaskRepository,
    private val internalSaver: InternalSaver
) : ViewModel() {
    private val handler = Handler(Looper.getMainLooper())


    val tasks: LiveData<Int> get() = taskRepository.observeTasksCountForDay(Date.today())
    val user: LiveData<User>
        get() = userRepository.observeUserById(ID)

    private val _progression = MutableLiveData(71)
    val progression: LiveData<Int>
        get() = _progression

    private val _photo = MutableLiveData<Bitmap?>(null)
    val photo: LiveData<Bitmap?>
        get() = _photo

    init {
        thread { loadPhotoFromInternal() }
    }

    private fun loadPhotoFromInternal() {
        try {
            val f = File(userRepository.getUserById(ID)?.photo, "ProfilePicture.png")
            Log.d(LOG_TAG, "Bitmap from ${f.absolutePath} is loading")
            val b = BitmapFactory.decodeStream(FileInputStream(f))
            handler.post { _photo.value = b }
            Log.d(LOG_TAG, "Bitmap from ${f.absolutePath} loaded")
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }

    fun onHabitsButtonClicked() {
        DI.getNavigator().navigateTo(MainNavigator.HABITS)
    }

    fun setName(name: String) {
        userRepository.getUserById(ID)?.let { user ->
            userRepository.updateUser(User(user.id, name, user.photo))
        }
    }

    fun loadPhoto(stream: InputStream, uri: String) {
        val user = userRepository.getUserById(ID)
        stream.use {
            _photo.value = BitmapFactory.decodeStream(it)
        }
        Log.d(
            LOG_TAG,
            "user: ${user}, bitmap: ${photo.value}"
        )
        thread {
            photo.value?.let { bitmap ->
                Log.d(LOG_TAG, "bitmap $bitmap is saving to the memory")
                val uri = internalSaver.saveToInternal(bitmap, "ProfilePicture")
                Log.d(LOG_TAG, "bitmap $bitmap saved to $uri")
                user?.let { user ->
                    userRepository.updateUser(User(user.id, user.name, uri))
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
                    DI.getUserRepository(), DI.getTaskRepository(), DI.getInternalSaver()
                ) as T
            }
        }
    }
}
