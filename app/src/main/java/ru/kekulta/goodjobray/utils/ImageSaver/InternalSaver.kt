package ru.kekulta.goodjobray.utils.ImageSaver

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream

fun saveToInternal(bitmap: Bitmap, context: Context, name: String): String {
    val wrapper = ContextWrapper(context)
    val directory = wrapper.getDir("imageDir", Context.MODE_PRIVATE)
    val path = File(directory, "$name.png")
    val fos = FileOutputStream(path).use {
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
    }
    return directory.absolutePath
}