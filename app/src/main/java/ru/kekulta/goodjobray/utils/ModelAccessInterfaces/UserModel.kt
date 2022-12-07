package ru.kekulta.goodjobray.utils.ModelAccessInterfaces

import androidx.lifecycle.LiveData
import ru.kekulta.goodjobray.data.User
import ru.kekulta.goodjobray.data.UserDao

interface UserModel {
    val user: User?
    val photo: String?
    val name: LiveData<String>
    fun setPhoto(uri: String)
    fun setName(name: String)
    fun loadUser(id: Int, dao: UserDao)
    fun saveUser(dao: UserDao)
}