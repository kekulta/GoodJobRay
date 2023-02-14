package ru.kekulta.goodjobray.screens.notes.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.kekulta.goodjobray.di.DI
import ru.kekulta.goodjobray.activity.data.Note
import kotlin.random.Random

class NotesViewModel() : ViewModel() {
    private val noteRepository = DI.getNoteRepository()

    private val _pinnedNotes =
        MutableLiveData(noteRepository.pinnedNotes.sortedByDescending { it.time })
    val pinnedNotes get(): LiveData<List<Note>> = _pinnedNotes

    private val _notes =
        MutableLiveData(noteRepository.notes.sortedByDescending { it.time })
    val notes get(): LiveData<List<Note>> = _notes

    init {
        noteRepository.addObserver { updateNotes() }
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

    private fun updateNotes() {
        println("notes updated")
        _pinnedNotes.value = noteRepository.pinnedNotes.sortedByDescending { it.time }
        _notes.value = noteRepository.notes.sortedByDescending { it.time }
    }

    fun deleteNote(note: Note) {
        noteRepository.deleteNote(note)
    }

    fun changePin(note: Note) {
        when (note.pinned) {
            true -> noteRepository.unpinNote(note)
            false -> noteRepository.pinNote(note)
        }
    }


}