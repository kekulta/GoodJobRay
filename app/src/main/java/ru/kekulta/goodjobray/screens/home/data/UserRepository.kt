package ru.kekulta.goodjobray.screens.home.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.kekulta.goodjobray.activity.data.User
import ru.kekulta.goodjobray.activity.data.UserDao
import ru.kekulta.goodjobray.utils.ID


class UserRepository(private val dao: UserDao) {
    private var _name: MutableLiveData<String> = MutableLiveData()
    private var _photo: String? = null

    val user: User?
        get() {
            val id = ID
            val name = _name.value

            return if (name != null) User(
                id,
                name,
                _photo
            ) else null
        }

    val photo: String?
        get() = _photo

    val name: LiveData<String> get() = _name

    init {
        val user = dao.getUser(ID)
        if (user == null) dao.insert(User(ID, "Username"))
        _name.value = user?.name ?: "Username"
        _photo = user?.photo
    }

    fun setName(name: String) {
        _name.value = name
    }

    fun setPhoto(uri: String) {
        _photo = uri
    }

    fun saveUser() {
        println("Request for save user: $user")
        user?.let {
            dao.insert(it)
            println("User: $it saved")
        }

    }
}

