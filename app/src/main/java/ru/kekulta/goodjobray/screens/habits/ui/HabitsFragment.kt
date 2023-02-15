package ru.kekulta.goodjobray.screens.habits.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import ru.kekulta.goodjobray.di.DI
import ru.kekulta.goodjobray.R
import ru.kekulta.goodjobray.screens.habits.presentation.HabitsViewModel
import ru.kekulta.goodjobray.screens.home.ui.HomeFragment

class HabitsFragment : Fragment() {

    // TODO (22) Думаю, что и в других фрагментах аналогично - заюзай Kotlin-делегат
    private lateinit var viewModel: HabitsViewModel
    private lateinit var callback: OnBackPressedCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callback = requireActivity().onBackPressedDispatcher.addCallback {
            // TODO (23) - навигация это бизнес-логика или UI?.. везде придерживайяся единого подхода
            DI.getNavigator().setScreen(HomeFragment::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // TODO (24) А почему тут без ViewBinding? По-хорошему, весь проект должен юзать ОДИН И ТОТ же подход ко всему.
        return inflater.inflate(R.layout.fragment_habits, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(HabitsViewModel::class.java)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // TODO (25) странно, что создаётся колбек в onCreate, а удаляется в onDestroyView -
        //  это не "симметричные" колбеки.
        callback.remove()
    }


}
