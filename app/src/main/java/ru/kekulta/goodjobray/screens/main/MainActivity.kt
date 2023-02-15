package ru.kekulta.goodjobray.screens.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.commit
import ru.kekulta.goodjobray.screens.planner.ui.PlannerFragment
import ru.kekulta.goodjobray.R
import ru.kekulta.goodjobray.databinding.ActivityMainBinding
import ru.kekulta.goodjobray.screens.home.presentation.PhotoPicker
import ru.kekulta.goodjobray.screens.notes.ui.NotesFragment

// TODO (20) MainActivity - это такая же ФИЧА, как и другие твои экраны. По-хорошему, у тебя должен был быть пакет .features.main
//   И в него ты бы поместил MainActivity / MainViewModel / Navigator
class MainActivity : AppCompatActivity() {

    // TODO (12) Binding разве не нужно очищать в onDestroy / onDestroyView ?
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    // TODO (16) не знаю как тебя, а меня лично бесит такое писать, дублировать кучу свойств непонятно ради чего.
    private var _navigator: Navigator? = null
    private val navigator get() = _navigator!!


    init {
        //Другие способы доставать фото из галереи?
        PhotoPicker.init(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Context -> Application Context

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



//        initViewModel()

        initNavigationBar()

        initScreenChanger()
    }


    private fun initNavigationBar() {
        // TODO (15) Так у тебя навигатор - это часть бизнес-логики или UI, определись)
        //   Я вижу, что навигатор инжектится в ViewModel -> и я думал, что у тебя логика по смене экранов тогда ТОЖЕ
        //   будет во ViewModel, но хоп и она в Activity.
        //   Если бы навигатор менял экраны ВНУТРИ ViewModel -> разбиение было бы более чётким.
        //   То есть Activity навесило click listener, при нажатии ты вызываешь метод ViewModel, та -
        //   дёргает внутри себя навигатор и меняет экран. И, поскольку у тебя уже есть observe навигации,
        //   то всё автоматом сменится
//        _navigator = DI.getNavigator()

        binding.homeBt.setOnClickListener {
            viewModel.onHomeButtonClick()

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
        // TODO (13) Вроде в Kotlin-е есть удобный делегат для доставания ViewModel :thinking_face:
//        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private fun initLayout() {
        // TODO (14) в таких маленьких классах как твой разбиение на мелкие методы только вредит, честно говоря)

    }



}
