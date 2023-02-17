package ru.kekulta.goodjobray.screens.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.commit
import ru.kekulta.goodjobray.screens.planner.ui.PlannerFragment
import ru.kekulta.goodjobray.R
import ru.kekulta.goodjobray.databinding.ActivityMainBinding
import ru.kekulta.goodjobray.di.DI
import ru.kekulta.goodjobray.screens.home.presentation.PhotoPicker
import ru.kekulta.goodjobray.screens.main.navigator.MainNavigator
import ru.kekulta.goodjobray.screens.notes.ui.NotesFragment

// TODO (20) MainActivity - это такая же ФИЧА, как и другие твои экраны. По-хорошему, у тебя должен был быть пакет .features.main
//   И в него ты бы поместил MainActivity / MainViewModel / Navigator
class MainActivity : AppCompatActivity() {

    // TODO (12) Binding разве не нужно очищать в onDestroy / onDestroyView ?
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()

    init {

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Context -> Application Context

        DI.initNavigator(supportFragmentManager, R.id.fragmentContainerView, MainNavigator.HOME)

        PhotoPicker.init(this)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        initNavigationBar()

    }

    private fun initNavigationBar() {
        // TODO (15) Так у тебя навигатор - это часть бизнес-логики или UI, определись)
        //   Я вижу, что навигатор инжектится в ViewModel -> и я думал, что у тебя логика по смене экранов тогда ТОЖЕ
        //   будет во ViewModel, но хоп и она в Activity.
        //   Если бы навигатор менял экраны ВНУТРИ ViewModel -> разбиение было бы более чётким.
        //   То есть Activity навесило click listener, при нажатии ты вызываешь метод ViewModel, та -
        //   дёргает внутри себя навигатор и меняет экран. И, поскольку у тебя уже есть observe навигации,
        //   то всё автоматом сменится

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


}
