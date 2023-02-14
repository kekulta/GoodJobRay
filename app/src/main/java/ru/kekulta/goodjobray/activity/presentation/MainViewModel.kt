package ru.kekulta.goodjobray.activity.presentation

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.kekulta.goodjobray.di.DI

// Ui - Activity / Fragment
// ViewModel -> Presentation
// Model -> Data / Domain

class MainViewModel : ViewModel() {
    private val navigator = DI.getNavigator()
    val screen: LiveData<Class<out Fragment>>
        get() = navigator.screen

}
