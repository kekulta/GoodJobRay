package ru.kekulta.goodjobray.screens.habits.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import ru.kekulta.goodjobray.di.DI
import ru.kekulta.goodjobray.R
import ru.kekulta.goodjobray.databinding.ActivityMainBinding
import ru.kekulta.goodjobray.databinding.FragmentHabitsBinding
import ru.kekulta.goodjobray.screens.habits.presentation.HabitsViewModel
import ru.kekulta.goodjobray.screens.home.ui.HomeFragment

class HabitsFragment : Fragment() {

    private val viewModel: HabitsViewModel by viewModels({ requireActivity() })

    private var _binding: FragmentHabitsBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHabitsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
