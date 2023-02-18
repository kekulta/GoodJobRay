package ru.kekulta.goodjobray.screens.notes.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import ru.kekulta.goodjobray.di.DI
import ru.kekulta.goodjobray.activity.data.Note
import kotlin.random.Random

class NotesViewModel : ViewModel() {
    private val noteRepository = DI.getNoteRepository()

    val pinnedNotes
        get(): LiveData<List<Note>> = noteRepository.observePinnedNotes().map { notes ->
            notes.sortedByDescending { it.creationTime }
        }


    val notes
        get(): LiveData<List<Note>> = noteRepository.observeNotes().map { notes ->
            notes.sortedByDescending { it.creationTime }
        }


    fun addNote(title: String, text: String) {
        noteRepository.addNote(
            Note(
                id = Random.nextInt(),
                title = title,
                text = text
            )
        )
    }

    fun deleteNote(note: Note) {
        noteRepository.deleteNote(note)
    }

    fun changePin(note: Note) {
        when (note.isPinned) {
            true -> noteRepository.unpinNote(note)
            false -> noteRepository.pinNote(note)
        }
    }


}