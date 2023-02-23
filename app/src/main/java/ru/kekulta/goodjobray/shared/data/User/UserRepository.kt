package ru.kekulta.goodjobray.shared.data.User

import androidx.lifecycle.distinctUntilChanged
import ru.kekulta.goodjobray.shared.data.models.User
import ru.kekulta.goodjobray.shared.data.dao.UserDao

class UserRepository(private val dao: UserDao) {
    fun observeUserById(id: Int) = dao.getUserLiveData(id)

    fun getUserById(id: Int) = dao.getUser(id)

    fun addUser(user: User) {
        dao.insert(user)
    }

    fun updateUser(user: User) {
        dao.update(user)
    }

    fun isExistById(id: Int): Boolean = dao.isExist(id)
}

