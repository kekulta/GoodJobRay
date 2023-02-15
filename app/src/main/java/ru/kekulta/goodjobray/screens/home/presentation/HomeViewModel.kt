package ru.kekulta.goodjobray.screens.home.presentation

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.kekulta.goodjobray.di.DI
import ru.kekulta.goodjobray.utils.Day
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException

import kotlin.concurrent.thread


class HomeViewModel : ViewModel() {



    init {
        println("HomeViewModel created")
    }

    private val handler = Handler(Looper.getMainLooper())
    private val userRepository = DI.getUserRepository()
    private val taskRepository = DI.getTaskRepository()

    private val _tasks = MutableLiveData(
        taskRepository.getTasksForDay(
            Day.actualDay,
            Day.actualMonth,
            Day.actualYear
        ).size
    )
    var photoUpdated = false

    val tasks: LiveData<Int> get() = _tasks
    val name: LiveData<String> get() = userRepository.name

    private val _progression = MutableLiveData(71)
    val progression: LiveData<Int>
        get() = _progression

    private val _photo = MutableLiveData<Bitmap?>(null)
    val photo: LiveData<Bitmap?>
        get() = _photo

    init {
        taskRepository.addObserver { updateTasks() }

        println("photo from memory: ${userRepository.photo}")
        thread { loadPhotoFromInternal() }
    }

    private fun loadPhotoFromInternal() {
        try {
            val f = File(userRepository.photo, "ProfilePicture.png")
            val b = BitmapFactory.decodeStream(FileInputStream(f))
            println("Photo downloaded from memory")
            handler.post { _photo.value = b }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }

    private fun updateTasks() {
        _tasks.value = taskRepository.getTasksForDay(
            Day.actualDay,
            Day.actualMonth,
            Day.actualYear
        ).size
    }

    fun setPhoto(uri: String) {
        userRepository.setPhoto(uri)

    }

    fun setName(name: String) {
        userRepository.setName(name)
    }


    fun pickPhoto() {
        PhotoPicker.getPhoto { bitmap, uri ->
            println("new photo: $uri, bitmap: $bitmap")
            bitmap?.let {
                photoUpdated = true
                _photo.value = it
            }
        }
    }
}
