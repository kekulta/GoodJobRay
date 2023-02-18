package ru.kekulta.goodjobray.shared.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = User.USERS
)
data class User(
    @ColumnInfo(name = ID) @PrimaryKey val id: Int,
    @ColumnInfo(name = NAME) val name: String = "Username",
    @ColumnInfo(name = PHOTO) val photo: String? = null,
) {
    companion object {
        const val USERS = "users"
        const val ID = "user_id"
        const val NAME = "user_name"
        const val PHOTO = "user_photo"
    }
}