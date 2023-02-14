package ru.kekulta.goodjobray.screens.notes.data


import ru.kekulta.goodjobray.activity.data.Note
import ru.kekulta.goodjobray.activity.data.NoteDao
import ru.kekulta.goodjobray.utils.DataObservable


class NoteRepository(private val dao: NoteDao) : DataObservable {

    private val observers = mutableListOf<() -> Unit>()

    val pinnedNotes: List<Note>
        get() = dao.getPinnedNotes()
    val notes: List<Note>
        get() = dao.getNotes()


    init {
        println(

            "notes loaded with notes: ${
                notes.filter { it.text == "" }.map { it.id }
            }, ${notes.filter { it.text == "" }.size}"
        )
        notifyObservers()
    }

    fun pinNote(note: Note) {
        dao.insert(
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

    fun unpinNote(note: Note) {
        dao.insert(
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

    fun addNote(note: Note) {
        println("adding note: $note")
        dao.insert(note)
        notifyObservers()
    }

    fun deleteNote(note: Note) {
        dao.delete(note)
        notifyObservers()
    }


    override fun addObserver(observer: () -> Unit) {
        observers.add(observer)
    }

    override fun removeObserver(observer: () -> Unit) {
        observers.remove(observer)
    }

    override fun notifyObservers() {
        println("notify observers with notes: ${notes.filter { it.text == "" }}, ${notes.filter { it.text == "" }.size}")
        observers.let { observers ->
            observers.forEach { observer ->
                observer.invoke()
            }
        }
    }
}