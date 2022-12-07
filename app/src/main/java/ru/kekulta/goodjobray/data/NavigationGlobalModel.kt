package ru.kekulta.goodjobray.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.kekulta.goodjobray.utils.Screen

object NavigationGlobalModel {
    private val _screen = MutableLiveData<Screen>(Screen.HOME)
    val screen: LiveData<Screen>
        get() = _screen

    fun setHomeScreen() {
        _screen.value = Screen.HOME
    }
    fun setPlannerScreen() {
        _screen.value = Screen.PLANNER
    }
    fun setNotesScreen() {
        _screen.value = Screen.NOTES
    }

    fun setHabitsScreen() {
        _screen.value = Screen.HABITS
    }
}