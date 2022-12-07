package ru.kekulta.goodjobray.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.kekulta.goodjobray.utils.ModelAccessInterfaces.UserModel

object UserGlobalModel : UserModel {
    private var _id: Int? = null
    private var _name: MutableLiveData<String> = MutableLiveData()
    private var _photo: String? = null

    override val user: User?
        get() {
            val id = _id
            val name = _name.value

            return if (id != null && name != null) User(
                id,
                name,
                _photo
            ) else null
        }

    override val photo: String?
        get() = _photo

    override val name: LiveData<String> get() = _name

    override fun setName(name: String) {
        _name.value = name
    }

    override fun setPhoto(uri: String) {
        _photo = uri
    }

    override fun saveUser(dao: UserDao) {
        println("Request for save user: $user")
        user?.let {
            dao.insert(it)
            println("User: $it saved")
        }

    }

    override fun loadUser(id: Int, dao: UserDao) {
        val user = dao.getUser(id)
        if (user == null) dao.insert(User(id, "Username"))
        _id = id
        _name.value = user?.name ?: "Username"
        _photo = user?.photo
    }
}

