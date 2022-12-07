package ru.kekulta.goodjobray.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.ViewModelProvider
import ru.kekulta.goodjobray.screens.planner.PlannerFragment
import ru.kekulta.goodjobray.R
import ru.kekulta.goodjobray.data.*
import ru.kekulta.goodjobray.databinding.ActivityMainBinding
import ru.kekulta.goodjobray.screens.HabitsFragment.HabitsFragment
import ru.kekulta.goodjobray.screens.home.HomeFragment
import ru.kekulta.goodjobray.screens.notes.NotesFragment
import ru.kekulta.goodjobray.utils.ImageSaver.instantiatePhotoPicker
import ru.kekulta.goodjobray.utils.Screen


const val ID: Int = 123456

const val LOREM =
    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."


class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var database: TaskDatabase
    private lateinit var taskDao: TaskDao
    private lateinit var userDao: UserDao
    private lateinit var noteDao: NoteDao

    private val tasksRep = MutableList<Task>(3) {
        Task(
            id = it,
            title = "task#$it",
            numOfMonth = (10 + it) % 12,
            dayOfMonth = it % 2 + 2,
            fromHour = it,
            toHour = it + 3,
            year = if (it == 2) 2023 else 2022
        )
    }
    private val notesRep = List(60) {
        Note(
            id = it,
            title = "Title#$it",
            text = "Text#$it ${LOREM.substring((30 * it) % LOREM.lastIndex)}",
            pinned = it % 2 == 0
        )
    }

    init {
        instantiatePhotoPicker(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initDatabase(this)

        initLayout()

        initModels()

        initNavigationBar()

        initScreenChanger()
    }


    override fun onStop() {
        super.onStop()
        println("onStop")
        saveAllChanges()
    }

    private fun initNavigationBar() {
        binding.homeBt.setOnClickListener {
            viewModel.setHomeScreen()
        }

        binding.plannerBt.setOnClickListener {
            viewModel.setPlannerScreen()
        }

        binding.notesBt.setOnClickListener {
            viewModel.setNotesScreen()
        }

    }

    private fun initScreenChanger() {
        viewModel.screen.observe(this) { screen ->
            when (screen) {
                Screen.HOME -> {
                    supportFragmentManager.commit {
                        replace<HomeFragment>(R.id.fragmentContainerView)
                    }

                }
                Screen.PLANNER -> {
                    supportFragmentManager.commit {
                        replace<PlannerFragment>(R.id.fragmentContainerView)
                    }
                }
                Screen.NOTES -> {
                    supportFragmentManager.commit {
                        replace<NotesFragment>(R.id.fragmentContainerView)

                    }
                }
                Screen.HABITS -> {
                    supportFragmentManager.commit {
                        replace<HabitsFragment>(R.id.fragmentContainerView)
                    }
                }
                else -> {
                    supportFragmentManager.commit {
                        replace<HomeFragment>(R.id.fragmentContainerView)
                    }
                }
            }
        }
    }

    private fun initModels() {
        database = getDatabase(this)
        taskDao = database.getTaskDao()
        userDao = database.getUserDao()
        noteDao = database.getNoteDao()

        UserGlobalModel.loadUser(ID, userDao)
        TaskGlobalModel.loadTasks(taskDao)
        NoteGlobalModel.loadNotes(noteDao)

        //tasksRep.forEach { taskDao.insert(it) }
        //notesRep.forEach { noteDao.insert(it) }

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private fun initLayout() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun saveAllChanges() {
        TaskGlobalModel.saveTaskChanges(taskDao)
        UserGlobalModel.saveUser(userDao)
        NoteGlobalModel.saveNotes(noteDao)
    }


}