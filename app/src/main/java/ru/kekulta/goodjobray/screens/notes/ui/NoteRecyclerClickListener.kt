package ru.kekulta.goodjobray.screens.notes.ui

import ru.kekulta.goodjobray.activity.data.Note

interface NoteRecyclerClickListener {
    fun onClick(index: Int, note: Note)
    fun onLongClick(index: Int, note: Note): Boolean
}