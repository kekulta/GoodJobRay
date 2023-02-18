package ru.kekulta.goodjobray.screens.home.data

import ru.kekulta.goodjobray.shared.data.models.User
import ru.kekulta.goodjobray.shared.data.dao.UserDao

const val ID = 123456

class UserRepository(private val dao: UserDao) {
    fun observeUserById(id: Int) = dao.getUserLiveData(id)

    fun getUserById(id: Int) = dao.getUser(id)

    fun addUser(user: User) {
        dao.insert(user)
    }

    fun updateUser(user: User) {
        dao.update(user)
    }
}

