package ru.kekulta.goodjobray.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import ru.kekulta.goodjobray.App
import ru.kekulta.goodjobray.di.DI
import ru.kekulta.goodjobray.screens.home.data.UserRepository
import ru.kekulta.goodjobray.screens.main.navigator.MainNavigator

class MainViewModel : ViewModel() {

    fun onHomeButtonClick() {
        App.INSTANCE.getNavigator().navigateTo(MainNavigator.HOME)
    }

    fun onPlannerButtonClick() {
        App.INSTANCE.getNavigator().navigateTo(MainNavigator.PLANNER)
    }

    fun onNotesButtonClick() {
        App.INSTANCE.getNavigator().navigateTo(MainNavigator.NOTES)

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

                ) as T
            }
        }
    }
}
