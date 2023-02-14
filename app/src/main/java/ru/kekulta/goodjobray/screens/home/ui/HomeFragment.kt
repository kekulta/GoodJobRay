package ru.kekulta.goodjobray.screens.home.ui

import android.graphics.Bitmap
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import ru.kekulta.goodjobray.di.DI
import ru.kekulta.goodjobray.databinding.FragmentHomeBinding
import ru.kekulta.goodjobray.screens.habits.ui.HabitsFragment
import ru.kekulta.goodjobray.screens.home.presentation.HomeViewModel
import ru.kekulta.goodjobray.screens.home.presentation.PhotoPicker
import kotlin.concurrent.thread

class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)

        setOnClickListeners()

        setObservers()
    }

    private fun setOnClickListeners() {
        binding.habitsCv.setOnClickListener {
            DI.getNavigator().setScreen(HabitsFragment::class.java)
        }

        binding.helloTv.setOnLongClickListener {
            enterNewName()
            true
        }

        binding.profilePv.setOnClickListener {
            println("Click on Pv received")
        }

        binding.profilePv.setOnLongClickListener() {
            println("Click on picture received")
            viewModel.pickPhoto()
            true
        }
    }

    private fun setObservers() {
        viewModel.photo.observe(viewLifecycleOwner) { bitmap ->
            println("photo observed: $bitmap")

            bitmap?.let {
                if (viewModel.photoUpdated) {
                    viewModel.photoUpdated = false
                    thread {
                        savePhoto(bitmap)
                        println("Photo saved back to memory")
                    }
                }
                binding.profilePv.bitmap = it
            }
        }


        viewModel.tasks.observe(viewLifecycleOwner) {
            val underlinedText = "$it task${if (it > 1) "s" else ""}"
            val fullText = "You have $underlinedText for today"

            binding.profilePv.text = fullText
            binding.profilePv.setUnderline(9, 8 + underlinedText.length)
        }

        viewModel.name.observe(viewLifecycleOwner) {
            binding.helloTv.text = "Welcome back, $it!"
        }


        viewModel.progression.observe(viewLifecycleOwner) {
            val progress: Float = it / 100f
            binding.progressCb.progress = progress
        }
    }

    private fun enterNewName() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Enter your name!")

        val input = EditText(requireContext())
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton(
            "OK"
        ) { _, _ ->
            val name = input.text.toString()
            viewModel.setName(name)
        }
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    private fun savePhoto(bitmap: Bitmap) {
        viewModel.setPhoto(PhotoPicker.saveToInternal(bitmap, requireContext(), "ProfilePicture"))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}