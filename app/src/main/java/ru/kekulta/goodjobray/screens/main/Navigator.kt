package ru.kekulta.goodjobray.screens.main

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.kekulta.goodjobray.screens.home.ui.HomeFragment

// TODO (21) Имя Navigator - супер-общее. Как будто это какой-то супер-глобальный навигатор, который будет действовать во всех случаях.
//  Но он у тебя относится к фиче MAIN -> я бы переименовал в MainNavigator
class Navigator {
    // TODO (10) Как ты себе представляешь хранение ФРАГМЕНТОВ в DI-графе?
    //  У тебя очень много данных лишних будет храниться, GC не сможет очистить память от этого.
    //  Именно по этой причине навигацию обычно абстрагируют, делают какой-то дополнительный класс/интерфейс Screen,
    //  который просто предоставляет тебе возможность построить граф навигации
    private val _screen = MutableLiveData<Class<out Fragment>>(HomeFragment::class.java)
    val screen: LiveData<Class<out Fragment>>
        get() = _screen

    fun setScreen(screen: Class<out Fragment>) {
        _screen.value = screen
    }
}
