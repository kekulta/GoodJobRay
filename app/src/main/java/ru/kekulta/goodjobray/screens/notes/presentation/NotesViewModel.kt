package ru.kekulta.goodjobray.screens.notes.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.kekulta.goodjobray.di.DI
import ru.kekulta.goodjobray.activity.data.Note
import ru.kekulta.goodjobray.screens.notes.data.NoteRepository

class NotesViewModel(private val noteRepository: NoteRepository) : ViewModel() {
    private val pinnedNotes
        get(): LiveData<List<Note>> = noteRepository.observePinnedNotes().map { notes ->
            notes.sortedByDescending { it.creationTime }
        }


    private val notes
        get(): LiveData<List<Note>> = noteRepository.observeNotes().map { notes ->
            notes.sortedByDescending { it.creationTime }
        }

    private val notesScreenState = MediatorLiveData<NotesScreenState>()

    init {
        notesScreenState.addSource(notes) { notes ->
            notesScreenState.value =
                notesScreenState.value?.copy(notes = notes) ?: NotesScreenState(notes = notes)

        }

        notesScreenState.addSource(pinnedNotes) { pinnedNotes ->
            notesScreenState.value =
                notesScreenState.value?.copy(pinnedNotes = pinnedNotes) ?: NotesScreenState(
                    pinnedNotes = pinnedNotes
                )
        }
    }

    fun observeState(): LiveData<NotesScreenState> = notesScreenState

    fun addNote(title: String, text: String) {
        noteRepository.addNote(
            Note(
                id = 0,
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

    companion object {
        const val LOG_TAG = "NotesViewModel"

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                NotesViewModel(
                    DI.getNoteRepository()
                )
            }
        }
    }

}