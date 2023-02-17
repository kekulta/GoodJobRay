package ru.kekulta.goodjobray.activity.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    // TODO (4) Обычно синтаксис SQL-я пишут с большой буквы, просто такой принятый codestyle
    //  SELECT * FROM ...
    // TODO Когда пишешь SQL-выражения для Room очень удобно пользоваться фичой котлина - multiline string
    // @Query("""
    //        SELECT *
    //        FROM User
    //        WHERE id = :id
    //    """)
    // TODO (5) Вынеси константы названий колонок и таблиц в объекты и используй в SQL-выражениях именно их.
    //   Это поможет разом поменять какие-то названия если что и не сломать при этом SQL-выражения.
    /*

    object TablesNames {
        const val USER = "User"
        const val TASK = "Task"
    }

     */
    @Query("""
        select * 
        from User 
        where id = :id
    """)
    fun getUser(id: Int): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)
}