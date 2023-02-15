package ru.kekulta.goodjobray.screens.main

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.kekulta.goodjobray.screens.home.ui.HomeFragment

class MainViewModel(
    private val navigator: Navigator,
) : ViewModel() {

    // TODO (9) Inject зависимости в ПОЛЕ - плохая практика. Лучше делать Inject в КОНСТРУКТОР.
    //  Это позволяет подменять какие-то зависимости по мере необходимости. Разберись с ViewModelProvider.Factory
//    private val navigator = DI.getNavigator()

    // TODO (8) Считается хорошей практикой держать ViewModel и Presenter-ы очищенными от Android SDK-классов, чтобы улучшить их тестируемость.
    //   Но тут нужно думать, у тебя вся навигация на этом построена.
    //  Что не нравится - то, что у тебя как-то супер-много всяких LiveData, прям обмазано всё)
    val screen: LiveData<Class<out Fragment>>
        get() = navigator.screen

    fun onHomeButtonClick() {

        navigator.setScreen(HomeFragment::class.java)

    }

}
