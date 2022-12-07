package ru.kekulta.goodjobray.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.kekulta.goodjobray.data.NavigationGlobalModel
import ru.kekulta.goodjobray.screens.home.HomeFragment
import ru.kekulta.goodjobray.utils.Screen

class MainViewModel : ViewModel() {
    val screen: LiveData<Screen>
        get() = NavigationGlobalModel.screen

    fun setHomeScreen() {
        NavigationGlobalModel.setHomeScreen()
    }
    fun setPlannerScreen() {
        NavigationGlobalModel.setPlannerScreen()
    }
    fun setNotesScreen() {
        NavigationGlobalModel.setNotesScreen()
    }

    fun setHabitsScreen() {
        NavigationGlobalModel.setHabitsScreen()
    }
}