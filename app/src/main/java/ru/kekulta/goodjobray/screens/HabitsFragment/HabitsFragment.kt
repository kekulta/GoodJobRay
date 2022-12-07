package ru.kekulta.goodjobray.screens.HabitsFragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import ru.kekulta.goodjobray.R
import ru.kekulta.goodjobray.data.NavigationGlobalModel

class HabitsFragment : Fragment() {

    private lateinit var viewModel: HabitsViewModel
    private lateinit var callback: OnBackPressedCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callback = requireActivity().onBackPressedDispatcher.addCallback {
            NavigationGlobalModel.setHomeScreen()
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