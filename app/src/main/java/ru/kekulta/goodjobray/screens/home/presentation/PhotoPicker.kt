package ru.kekulta.goodjobray.screens.home.presentation

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher

import androidx.activity.result.contract.ActivityResultContracts
import java.io.File
import java.io.FileOutputStream


object PhotoPicker {
    private var callback: ((Bitmap?, String) -> Unit)? = null

    private val galleryIntent =
        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)


    private var activityResultLauncher: ActivityResultLauncher<Intent>? = null


    fun init(activity: ComponentActivity) {
        activityResultLauncher =
            activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val photoUri = result.data?.data ?: return@registerForActivityResult
                    activity.contentResolver.openInputStream(photoUri)?.use {
                        callback?.invoke(
                            BitmapFactory.decodeStream(it),
                            photoUri.toString()
                        )
                        callback = null
                    }
                }
            }
    }

    fun getPhoto(callback: (Bitmap?, String) -> Unit) {
        PhotoPicker.callback = callback
        activityResultLauncher.let {
            requireNotNull(it) { "PhotoPicker must be initialized" }
            it.launch(galleryIntent)
        }
    }

    fun saveToInternal(bitmap: Bitmap, context: Context, name: String): String {
        val wrapper = ContextWrapper(context)
        val directory = wrapper.getDir("imageDir", Context.MODE_PRIVATE)
        val path = File(directory, "$name.png")
        FileOutputStream(path).use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        }
        return directory.absolutePath
    }
}
