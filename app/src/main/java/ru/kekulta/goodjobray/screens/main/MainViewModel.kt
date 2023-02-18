package ru.kekulta.goodjobray.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import ru.kekulta.goodjobray.di.DI
import ru.kekulta.goodjobray.screens.home.data.UserRepository
import ru.kekulta.goodjobray.screens.main.navigator.MainNavigator

class MainViewModel(
    userRepository: UserRepository
) : ViewModel() {

    fun onHomeButtonClick() {
        DI.getNavigator().navigateTo(MainNavigator.HOME)
    }

    fun onPlannerButtonClick() {
        DI.getNavigator().navigateTo(MainNavigator.PLANNER)
    }

    fun onNotesButtonClick() {
        DI.getNavigator().navigateTo(MainNavigator.NOTES)

    }

    companion object {
        const val LOG_TAG = "MainViewModel"

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {

                return MainViewModel(
                    DI.getUserRepository()
                ) as T
            }
        }
    }
}
