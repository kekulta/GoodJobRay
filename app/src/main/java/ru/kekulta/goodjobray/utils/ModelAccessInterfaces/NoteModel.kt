package ru.kekulta.goodjobray.utils.ModelAccessInterfaces

import androidx.lifecycle.LiveData
import ru.kekulta.goodjobray.data.Note
import ru.kekulta.goodjobray.data.NoteDao

interface NoteModel {
    val pinnedNotes: List<Note>
    val otherNotes: List<Note>
    fun pinNote(note: Note)
    fun unpinNote(note: Note)
    fun addNote(note: Note)
    fun deleteNote(note: Note)

    fun loadNotes(dao: NoteDao)
    fun saveNotes(dao: NoteDao)
}