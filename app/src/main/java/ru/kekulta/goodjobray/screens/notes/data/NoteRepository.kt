package ru.kekulta.goodjobray.screens.notes.data


import androidx.lifecycle.LiveData
import ru.kekulta.goodjobray.activity.data.Note
import ru.kekulta.goodjobray.activity.data.NoteDao


class NoteRepository(private val dao: NoteDao) {

    fun observeNotes(): LiveData<List<Note>> =
        dao.getNotesLiveData()

    fun observePinnedNotes(): LiveData<List<Note>> =
        dao.getPinnedNotesLiveData()

    fun pinNote(note: Note) {
        dao.update(
            Note(
                id = note.id,
                title = note.title,
                text = note.text,
                isPinned = true,
                creationTime = note.creationTime
            )
        )
    }

    fun unpinNote(note: Note) {
        dao.update(
            Note(
                id = note.id,
                title = note.title,
                text = note.text,
                isPinned = false,
                creationTime = note.creationTime
            )
        )
    }

    fun addNote(note: Note) {
        println("adding note: $note")
        dao.insert(note)
    }

    fun deleteNote(note: Note) {
        dao.delete(note)
    }
}