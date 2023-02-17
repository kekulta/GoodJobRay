package ru.kekulta.goodjobray.screens.main.navigator

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.kekulta.goodjobray.R
import ru.kekulta.goodjobray.screens.habits.ui.HabitsFragment
import ru.kekulta.goodjobray.screens.home.ui.HomeFragment
import ru.kekulta.goodjobray.screens.notes.ui.NotesFragment
import ru.kekulta.goodjobray.screens.planner.ui.PlannerFragment
import java.lang.IllegalArgumentException


class MainNavigator(
    private val fragmentManager: FragmentManager,
    private val container: Int,
    startScreen: String
) {
    // TODO (10) Как ты себе представляешь хранение ФРАГМЕНТОВ в DI-графе?
    //  У тебя очень много данных лишних будет храниться, GC не сможет очистить память от этого.
    //  Именно по этой причине навигацию обычно абстрагируют, делают какой-то дополнительный класс/интерфейс Screen,
    //  который просто предоставляет тебе возможность построить граф навигации

    val currentScreen: String?
        get() {
            with(fragmentManager) {
                if (backStackEntryCount == 0) return null
                return getBackStackEntryAt(backStackEntryCount - 1).name
            }
        }

    init {
        fragmentManager.commit {
            replace(container, provideFragment(startScreen), startScreen)
        }
    }

    fun navigateTo(screenKey: String) {
        if (currentScreen == screenKey) return
        fragmentManager.commit {
            replace(container, provideFragment(screenKey))
            addToBackStack(screenKey)
        }
    }

    private fun provideFragment(screenKey: String): Fragment {
        return when (screenKey) {
            HOME -> HomeFragment()
            NOTES -> NotesFragment()
            PLANNER -> PlannerFragment()
            HABITS -> HabitsFragment()
            else -> throw IllegalArgumentException("Unknown screen key!")
        }
    }

    companion object {
        const val HOME = "screen_home"
        const val NOTES = "screen_notes"
        const val PLANNER = "screen_planner"
        const val HABITS = "screen_habits"
    }
}
