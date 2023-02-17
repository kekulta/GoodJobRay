package ru.kekulta.goodjobray.screens.main

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import ru.kekulta.goodjobray.di.DI
import ru.kekulta.goodjobray.screens.home.ui.HomeFragment
import ru.kekulta.goodjobray.screens.main.navigator.MainNavigator

class MainViewModel(

) : ViewModel() {

    // TODO (9) Inject зависимости в ПОЛЕ - плохая практика. Лучше делать Inject в КОНСТРУКТОР.
    //  Это позволяет подменять какие-то зависимости по мере необходимости. Разберись с ViewModelProvider.Factory

    // TODO (8) Считается хорошей практикой держать ViewModel и Presenter-ы очищенными от Android SDK-классов, чтобы улучшить их тестируемость.
    //   Но тут нужно думать, у тебя вся навигация на этом построена.
    //  Что не нравится - то, что у тебя как-то супер-много всяких LiveData, прям обмазано всё)

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
