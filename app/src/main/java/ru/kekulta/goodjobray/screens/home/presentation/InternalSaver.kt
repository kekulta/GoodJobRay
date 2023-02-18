package ru.kekulta.goodjobray.screens.home.presentation

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream

class InternalSaver(val context: Context) {
    fun saveToInternal(bitmap: Bitmap,  name: String): String {
        val wrapper = ContextWrapper(context)
        val directory = wrapper.getDir("imageDir", Context.MODE_PRIVATE)
        val path = File(directory, "$name.png")
        FileOutputStream(path).use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        }
        return directory.absolutePath
    }
}