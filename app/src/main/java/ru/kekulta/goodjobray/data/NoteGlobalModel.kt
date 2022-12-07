package ru.kekulta.goodjobray.data


import ru.kekulta.goodjobray.utils.ModelAccessInterfaces.DataObservable
import ru.kekulta.goodjobray.utils.ModelAccessInterfaces.NoteModel


object NoteGlobalModel : NoteModel, DataObservable {

    private val _pinnedNotes = mutableListOf<Note>()
    private val _otherNotes = mutableListOf<Note>()
    private val deletedNotes = mutableListOf<Note>()

    private val observers = mutableListOf<() -> Unit>()


    override val pinnedNotes: List<Note>
        get() = _pinnedNotes
    override val otherNotes: List<Note>
        get() = _otherNotes

    override fun pinNote(note: Note) {
        if (note in otherNotes) {
            _otherNotes.remove(note)
            _pinnedNotes.add(Note(
                id = note.id,
                title = note.title,
                text = note.text,
                pinned = true,
                time = note.time
            ))
            notifyObservers()
        }
    }

    override fun unpinNote(note: Note) {
        if (note in pinnedNotes) {
            _pinnedNotes.remove(note)
            _otherNotes.add(Note(
                id = note.id,
                title = note.title,
                text = note.text,
                pinned = false,
                time = note.time
            ))
            notifyObservers()
        }
    }

    override fun addNote(note: Note) {
        println("adding note: $note")
        when (note.pinned) {
            true -> _pinnedNotes.add(note)
            false -> _otherNotes.add(note)
        }
        notifyObservers()
    }

    override fun deleteNote(note: Note) {
        when (note.pinned) {
            true -> _pinnedNotes.remove(note)
            false -> _otherNotes.remove(note)
        }
        deletedNotes.add(note)
        notifyObservers()
    }

    override fun loadNotes(dao: NoteDao) {
        _pinnedNotes.addAll(dao.getPinnedNotes())
        _otherNotes.addAll(dao.getNotPinnedNotes())
        println("notes loaded with notes: ${otherNotes.filter { it.text == "" }.map { it.id }}, ${otherNotes.filter { it.text == "" }.size}")
        notifyObservers()
    }

    override fun saveNotes(dao: NoteDao) {
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