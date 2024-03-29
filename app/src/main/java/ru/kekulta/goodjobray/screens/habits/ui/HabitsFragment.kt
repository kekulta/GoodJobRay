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

    private lateinit var viewModel: HabitsViewModel
    private lateinit var callback: OnBackPressedCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callback = requireActivity().onBackPressedDispatcher.addCallback {
            DI.getNavigator().setScreen(HomeFragment::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_habits, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(HabitsViewModel::class.java)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        callback.remove()
    }


}
