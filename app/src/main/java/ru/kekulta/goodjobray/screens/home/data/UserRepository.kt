package ru.kekulta.goodjobray.screens.home.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.kekulta.goodjobray.activity.data.User
import ru.kekulta.goodjobray.activity.data.UserDao
import ru.kekulta.goodjobray.utils.ID

// TODO FIXME - зачёт за избавление от передачи Dao в методы
class UserRepository(private val dao: UserDao) {

    // TODO (26) Смотри, Repository должен давать тебе возможность доставать или модифицировать данные.
    //  А вот ХРАНИТЬ их внутри себя - это уже какая-то "бизнес-логика", которая ближе к ViewModel.
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
        // TODO (27) Плохая практика - как-то вставлять данные на этапе инициализации DI
        //  (у тебя DI вызовет конструктор UserRepository, а ты в конструкторе какие-то данные вставляешь)
        //  Это тоже часть "бизнес-логики", которая должна быть ближе к ViewModel
        val user = dao.getUser(ID)
        if (user == null) dao.insert(User(ID, "Username"))
        _name.value = user?.name ?: "Username"
        _photo = user?.photo
    }

    fun setName(name: String) {
        _name.value = name
        saveUser()
    }

    // TODO (29) А если бы у твоего юзера было бы 30 разных свойств, и все могли бы меняться?
    //  Ты бы сделал 30 методов таких? Не надо так - сделай один метод `saveUser(user: User)`, передай в него объект,
    //  который должен быть сохранён и всё.
    fun setPhoto(uri: String) {
        _photo = uri
        saveUser()
    }

    // TODO (28) Не репозиторий должен внутри себя хранить данные о пользователе, а ViewModel.
    //  Ты в этот метод должен передавать объект User-а, который должен быть сохранён.
    private fun saveUser() {
        println("Request for save user: $user")
        user?.let {
            dao.insert(it)
            println("User: $it saved")
        }

    }
}

