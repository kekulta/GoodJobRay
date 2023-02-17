package ru.kekulta.goodjobray.activity.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    @PrimaryKey val id: Int,
    val title: String = "",
    val text: String = "",
    val pinned: Boolean = false,
    val time: Long = System.currentTimeMillis()
)