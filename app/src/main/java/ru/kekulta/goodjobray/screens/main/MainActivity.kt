package ru.kekulta.goodjobray.screens.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import ru.kekulta.goodjobray.App
import ru.kekulta.goodjobray.R
import ru.kekulta.goodjobray.databinding.ActivityMainBinding
import ru.kekulta.goodjobray.screens.main.navigator.MainNavigator


class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels { MainViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initNavigationBar()
    }

    override fun onResume() {
        super.onResume()
        App.INSTANCE.setNavigator(
            supportFragmentManager,
            R.id.fragmentContainerView,
            MainNavigator.HOME
        )
    }

    override fun onPause() {
        super.onPause()
        App.INSTANCE.removeNavigator()
    }

    private fun initNavigationBar() {
        binding.homeBt.setOnClickListener {
            viewModel.onHomeButtonClick()
        }

        binding.plannerBt.setOnClickListener {
            viewModel.onPlannerButtonClick()
        }

        binding.notesBt.setOnClickListener {
            viewModel.onNotesButtonClick()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
