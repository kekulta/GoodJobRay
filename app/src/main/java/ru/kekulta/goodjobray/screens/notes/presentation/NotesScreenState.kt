package ru.kekulta.goodjobray.screens.notes.presentation

import ru.kekulta.goodjobray.activity.data.Note


data class NotesScreenState(val notes: List<Note>? = null, val pinnedNotes: List<Note>? = null)