package ru.kekulta.goodjobray

import android.app.Application
import androidx.fragment.app.FragmentManager
import ru.kekulta.goodjobray.di.DI
import ru.kekulta.goodjobray.screens.main.navigator.MainNavigator
import ru.kekulta.goodjobray.shared.data.manager.UserManager


internal class App : Application() {

    private var navigator: MainNavigator? = null

    override fun onCreate() {
        super.onCreate()
        DI.initDi(this)
        INSTANCE = this

        createUser()
    }

    private fun createUser() {
        DI.getUserManager().forceSetUser(UserManager.ID)
    }

    fun setNavigator(fragmentManager: FragmentManager, container: Int, startScreen: String) {
        navigator = MainNavigator(fragmentManager, container, startScreen)
    }

    fun removeNavigator() {
        navigator = null
    }

    fun getNavigator(): MainNavigator {
        return requireNotNull(navigator) { "Navigator should be initialized" }
    }

    companion object {
        const val LOG_TAG = "Application"

        lateinit var INSTANCE: App
    }
}


