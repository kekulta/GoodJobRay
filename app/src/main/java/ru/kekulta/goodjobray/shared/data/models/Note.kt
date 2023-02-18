package ru.kekulta.goodjobray.activity.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = Note.NOTES
)
data class Note(
    @ColumnInfo(name = ID)
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = TITLE)
    var title: String = "",
    @ColumnInfo(name = TEXT)
    var text: String = "",
    @ColumnInfo(name = IS_PINNED)
    var isPinned: Boolean = false,
    @ColumnInfo(name = TIME)
    var creationTime: Long = System.currentTimeMillis()
) {


    companion object {
        const val NOTES = "notes"
        const val ID = "notes_id"
        const val TITLE = "notes_title"
        const val TEXT = "notes_text"
        const val IS_PINNED = "notes_is_pinned"
        const val TIME = "notes_time_creation"
    }
}