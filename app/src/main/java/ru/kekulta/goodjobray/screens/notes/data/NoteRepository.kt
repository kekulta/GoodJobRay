package ru.kekulta.goodjobray.screens.notes.data


import ru.kekulta.goodjobray.activity.data.Note
import ru.kekulta.goodjobray.activity.data.NoteDao
import ru.kekulta.goodjobray.utils.DataObservable


class NoteRepository(private val dao: NoteDao) : DataObservable {

    private val _pinnedNotes = mutableListOf<Note>()
    private val _otherNotes = mutableListOf<Note>()
    private val deletedNotes = mutableListOf<Note>()

    private val observers = mutableListOf<() -> Unit>()

    val pinnedNotes: List<Note>
        get() = _pinnedNotes
    val otherNotes: List<Note>
        get() = _otherNotes


    init {
        _pinnedNotes.addAll(dao.getPinnedNotes())
        _otherNotes.addAll(dao.getNotPinnedNotes())
        println(
            "notes loaded with notes: ${
                otherNotes.filter { it.text == "" }.map { it.id }
            }, ${otherNotes.filter { it.text == "" }.size}"
        )
        notifyObservers()
    }

    fun pinNote(note: Note) {
        if (note in otherNotes) {
            _otherNotes.remove(note)
            _pinnedNotes.add(
                Note(
                    id = note.id,
                    title = note.title,
                    text = note.text,
                    pinned = true,
                    time = note.time
                )
            )
            notifyObservers()
        }
    }

    fun unpinNote(note: Note) {
        if (note in pinnedNotes) {
            _pinnedNotes.remove(note)
            _otherNotes.add(
                Note(
                    id = note.id,
                    title = note.title,
                    text = note.text,
                    pinned = false,
                    time = note.time
                )
            )
            notifyObservers()
        }
    }

    fun addNote(note: Note) {
        println("adding note: $note")
        when (note.pinned) {
            true -> _pinnedNotes.add(note)
            false -> _otherNotes.add(note)
        }
        notifyObservers()
    }

    fun deleteNote(note: Note) {
        when (note.pinned) {
            true -> _pinnedNotes.remove(note)
            false -> _otherNotes.remove(note)
        }
        deletedNotes.add(note)
        notifyObservers()
    }


    fun saveNotes() {
        dao.delete(deletedNotes)
        dao.insert(_pinnedNotes)
        dao.insert(_otherNotes)
    }

    override fun addObserver(observer: () -> Unit) {
        observers.add(observer)
    }

    override fun removeObserver(observer: () -> Unit) {
        observers.remove(observer)
    }

    override fun notifyObservers() {
        println("notify observers with notes: ${otherNotes.filter { it.text == "" }}, ${otherNotes.filter { it.text == "" }.size}")
        observers.let { observers ->
            observers.forEach { observer ->
                observer.invoke()
            }
        }
    }
}