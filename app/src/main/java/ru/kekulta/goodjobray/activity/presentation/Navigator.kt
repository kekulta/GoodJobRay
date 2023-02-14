package ru.kekulta.goodjobray.activity.presentation

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.kekulta.goodjobray.screens.home.ui.HomeFragment

class Navigator {
    private val _screen = MutableLiveData<Class<out Fragment>>(HomeFragment::class.java)
    val screen: LiveData<Class<out Fragment>>
        get() = _screen

    fun setScreen(screen: Class<out Fragment>) {
        _screen.value = screen
    }
}