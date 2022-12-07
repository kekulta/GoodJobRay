package ru.kekulta.goodjobray.screens.home

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.kekulta.goodjobray.data.DateGlobalModel
import ru.kekulta.goodjobray.data.TaskGlobalModel
import ru.kekulta.goodjobray.data.UserGlobalModel
import ru.kekulta.goodjobray.utils.ImageSaver.pickPhotoFromMemory
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException

import kotlin.concurrent.thread


class HomeViewModel() : ViewModel() {



    init {
        println("HomeViewModel created")
    }

    private val handler = Handler(Looper.getMainLooper())
    private val userModel = UserGlobalModel
    private val taskModel = TaskGlobalModel
    private val dateModel = DateGlobalModel
    private val _tasks = MutableLiveData(
        taskModel.getTasksFor(
            dateModel.actualDay,
            dateModel.actualMonth,
            dateModel.actualYear
        ).size
    )
    var photoUpdated = false

    val tasks: LiveData<Int> get() = _tasks
    val name: LiveData<String> get() = userModel.name

    private val _progression = MutableLiveData<Int>(71)
    val progression: LiveData<Int>
        get() = _progression

    private val _photo = MutableLiveData<Bitmap?>(null)
    val photo: LiveData<Bitmap?>
        get() = _photo

    init {
        taskModel.addObserver { updateTasks() }

        println("photo from memory: ${userModel.photo}")
        thread { loadPhotoFromInternal() }
    }

    private fun loadPhotoFromInternal() {
        try {
            val f = File(userModel.photo, "ProfilePicture.png")
            val b = BitmapFactory.decodeStream(FileInputStream(f))
            println("Photo downloaded from memory")
            handler.post { _photo.value = b }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }

    private fun updateTasks() {
        _tasks.value = taskModel.getTasksFor(
            dateModel.actualDay,
            dateModel.actualMonth,
            dateModel.actualYear
        ).size
    }

    fun setPhoto(uri: String) {
        userModel.setPhoto(uri)

    }

    fun setName(name: String) {
        userModel.setName(name)
    }


    fun pickPhoto() {
        println(pickPhotoFromMemory { bitmap, _uri ->
            println("new photo: $_uri, bitmap: $bitmap")
            bitmap?.let {
                photoUpdated = true
                _photo.value = it
            }
        })
    }
}