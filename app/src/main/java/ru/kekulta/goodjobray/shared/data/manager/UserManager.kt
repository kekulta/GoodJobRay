package ru.kekulta.goodjobray.shared.data.manager

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import ru.kekulta.goodjobray.App
import ru.kekulta.goodjobray.screens.home.data.UserRepository
import ru.kekulta.goodjobray.shared.data.models.User

class UserManager(private val userRepository: UserRepository) {
    private var currentUser = MutableLiveData<Int>(null)
    fun getUser(): User {
        return userRepository.getUserById(requireNotNull(currentUser.value) { "User must be set to access its data" })!!
    }

    fun observeUser(): LiveData<User> =
        currentUser.switchMap { id ->
            userRepository.observeUserById(requireNotNull(id) { "User must be set to access its data" })
        }


    fun setUser(id: Int): Boolean {
        if (!userRepository.isExistById(id)) {
            return false
        }
        currentUser.value = id
        return true
    }

    fun updateUser(user: User) {
        require(user.id == requireNotNull(currentUser.value) { "User must be set to update its data" }) { "Id of updated user should match with current user" }
        userRepository.updateUser(user)
    }

    fun forceSetUser(id: Int): Boolean {
        val existed = userRepository.isExistById(id)
        if (!existed) {
            userRepository.addUser(User(id))
            Log.d(LOG_TAG, "There is no user with ID = $ID in DB, user inserted")
        }
        currentUser.value = id
        return existed
    }

    companion object {
        const val LOG_TAG = "UserManager"
        const val ID = 123456
    }
}

