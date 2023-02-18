package ru.kekulta.goodjobray

import android.app.Application
import android.util.Log
import ru.kekulta.goodjobray.shared.data.models.User
import ru.kekulta.goodjobray.di.DI
import ru.kekulta.goodjobray.screens.home.data.ID


// TODO (17) Application -> странно что в пакете .di
internal class App : Application() {
    override fun onCreate() {
        super.onCreate()
        DI.initDi(this)

        createUser()
    }

    private fun createUser() {
        if (DI.getUserRepository().getUserById(ID) == null) {
            DI.getUserRepository().addUser(User(ID))
            Log.d(LOG_TAG, "There is no user with ID = $ID in DB, user inserted")
        } else {
            Log.d(LOG_TAG, "User with ID = $ID found")
        }
    }

    companion object {
        const val LOG_TAG = "Application"
    }
}


