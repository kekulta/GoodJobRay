package ru.kekulta.goodjobray.utils.ImageSaver

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts


private lateinit var Instance: PhotoPicker

fun instantiatePhotoPicker(context: ComponentActivity): PhotoPicker {
    Instance = PhotoPicker(context)
    return Instance
}

fun pickPhotoFromMemory(callback: (Bitmap?, String) -> Unit) {
    if (!::Instance.isInitialized) throw java.lang.IllegalStateException("Photo picker must be initialized at LifeCycle owner")
    return Instance.getPhoto(callback)
}

class PhotoPicker internal constructor(context: ComponentActivity) {
    private var callback: ((Bitmap?, String) -> Unit)? = null

    private val galleryIntent =
        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)


    private val activityResultLauncher =
        context.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val photoUri = result.data?.data ?: return@registerForActivityResult
                context.contentResolver.openInputStream(photoUri)?.use {
                    callback?.invoke(
                        BitmapFactory.decodeStream(it),
                        photoUri.toString()
                    )
                    callback = null
                }
            }
        }

    fun getPhoto(callback: (Bitmap?, String) -> Unit) {
        this.callback = callback
        activityResultLauncher.launch(galleryIntent)
    }

}
