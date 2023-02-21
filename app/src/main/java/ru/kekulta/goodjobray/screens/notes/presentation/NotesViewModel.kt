package ru.kekulta.goodjobray.screens.notes.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import androidx.lifecycle.viewmodel.CreationExtras
import ru.kekulta.goodjobray.di.DI
import ru.kekulta.goodjobray.activity.data.Note
import ru.kekulta.goodjobray.screens.notes.data.NoteRepository
import ru.kekulta.goodjobray.screens.planner.presentation.PlannerViewModel
import kotlin.random.Random

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
            notesScreenState.value = NotesScreenState(notes, notesScreenState.value?.pinnedNotes)
        }

        notesScreenState.addSource(pinnedNotes) { pinnedNotes ->
            notesScreenState.value = NotesScreenState(notesScreenState.value?.notes, pinnedNotes)
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

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                return NotesViewModel(
                    DI.getNoteRepository()
                ) as T
            }
        }
    }

}