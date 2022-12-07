package ru.kekulta.goodjobray.screens.notes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.kekulta.goodjobray.data.Note
import ru.kekulta.goodjobray.data.NoteGlobalModel
import kotlin.random.Random

class NotesViewModel() : ViewModel() {
    private val model = NoteGlobalModel

    private val _pinnedNotes =
        MutableLiveData(model.pinnedNotes.sortedByDescending { it.time })
    val pinnedNotes get(): LiveData<List<Note>> = _pinnedNotes

    private val _notes =
        MutableLiveData(model.otherNotes.sortedByDescending { it.time })
    val notes get(): LiveData<List<Note>> = _notes

    init {
        model.addObserver { updateNotes() }
    }

    fun addNote(title: String, text: String) {
        model.addNote(
            Note(
                id = Random.nextInt(),
                title = title,
                text = text
            )
        )
    }

    private fun updateNotes() {
        println("notes updated")
        _pinnedNotes.value = model.pinnedNotes.sortedByDescending { it.time }
        _notes.value = model.otherNotes.sortedByDescending { it.time }
    }

    fun deleteNote(note: Note) {
        model.deleteNote(note)
    }

    fun changePin(note: Note) {
        when (note.pinned) {
            true -> model.unpinNote(note)
            false -> model.pinNote(note)
        }
    }


}