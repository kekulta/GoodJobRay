package ru.kekulta.goodjobray.activity.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import ru.kekulta.goodjobray.di.DI
import ru.kekulta.goodjobray.screens.planner.ui.PlannerFragment
import ru.kekulta.goodjobray.R
import ru.kekulta.goodjobray.databinding.ActivityMainBinding
import ru.kekulta.goodjobray.activity.presentation.MainViewModel
import ru.kekulta.goodjobray.activity.presentation.Navigator
import ru.kekulta.goodjobray.screens.home.presentation.PhotoPicker
import ru.kekulta.goodjobray.screens.home.ui.HomeFragment
import ru.kekulta.goodjobray.screens.notes.ui.NotesFragment

// 1 - пакеты по фичам разбить
// 2 - попробовать уйти от object-ов Model к Repository-классам
// 3 - инициализировать DI --> переделать глобальные модели на DI


class MainActivity : AppCompatActivity() {

    // lateinit или get!!
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    private var _navigator: Navigator? = null
    private val navigator get() = _navigator!!


    init {
        //Другие способы доставать фото из галереи?
        PhotoPicker.init(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Context -> Application Context

        initLayout()

        initViewModel()

        initNavigationBar()

        initScreenChanger()
    }


    override fun onStop() {
        super.onStop()
        println("onStop")
        saveAllChanges()
    }

    private fun initNavigationBar() {
        _navigator = DI.getNavigator()

        binding.homeBt.setOnClickListener {
            navigator.setScreen(HomeFragment::class.java)
        }

        binding.plannerBt.setOnClickListener {
            navigator.setScreen(PlannerFragment::class.java)
        }

        binding.notesBt.setOnClickListener {
            navigator.setScreen(NotesFragment::class.java)
        }

    }

    private fun initScreenChanger() {
        viewModel.screen.observe(this) { screen ->
            supportFragmentManager.commit {
                replace(R.id.fragmentContainerView, screen, null)
            }
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private fun initLayout() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun saveAllChanges() {
        DI.getTaskRepository().saveTaskChanges()
        DI.getUserRepository().saveUser()
        DI.getNoteRepository().saveNotes()
    }


}
