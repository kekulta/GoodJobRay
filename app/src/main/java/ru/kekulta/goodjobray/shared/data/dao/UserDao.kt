package ru.kekulta.goodjobray.shared.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.kekulta.goodjobray.shared.data.models.User

@Dao
interface UserDao {
    @Query(
        """
        SELECT * 
        FROM ${User.USERS} 
        WHERE ${User.ID} = :id
    """
    )
    fun getUserLiveData(id: Int): LiveData<User>

    @Query(
        value = """
        SELECT *
        FROM ${User.USERS} 
        WHERE ${User.ID} = :id
    """
    )
    fun getUser(id: Int): User?

    @Query(
        """
        SELECT EXISTS
                (SELECT * 
                FROM ${User.USERS} 
                WHERE ${User.ID} = :id)
                """
    )
    fun isExist(id: Int): Boolean


    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(user: User)

    @Update
    fun update(user: User)

}