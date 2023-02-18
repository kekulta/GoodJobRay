package ru.kekulta.goodjobray.screens.home.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import ru.kekulta.goodjobray.databinding.FragmentHomeBinding
import ru.kekulta.goodjobray.screens.home.presentation.HomeViewModel

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels({ requireActivity() }) { HomeViewModel.Factory }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var activityResultLauncher: ActivityResultLauncher<Intent>? = null

    private val galleryIntent =
        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val photoUri = result.data?.data ?: return@registerForActivityResult
                    requireActivity().contentResolver.openInputStream(photoUri)?.use { stream ->
                        viewModel.loadPhoto(stream, photoUri.toString())
                    }
                }
            }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()

        setObservers()
    }

    private fun setOnClickListeners() {
        binding.habitsCv.setOnClickListener {
            viewModel.onHabitsButtonClicked()
        }

        binding.helloTv.setOnLongClickListener {
            enterNewName()
            true
        }

        binding.profilePv.setOnLongClickListener {
            activityResultLauncher.let {
                requireNotNull(it) { "PhotoPicker must be initialized" }
                it.launch(galleryIntent)
            }
            true
        }
    }

    private fun setObservers() {
        viewModel.photo.observe(viewLifecycleOwner) { bitmap ->

            Log.d(LOG_TAG, "bitmap observed: $bitmap")

            bitmap?.let {
                binding.profilePv.bitmap = it
            }
        }

        viewModel.tasks.observe(viewLifecycleOwner) {
            val underlinedText = "$it task${if (it > 1) "s" else ""}"
            val fullText = "You have $underlinedText for today"

            binding.profilePv.text = fullText
            binding.profilePv.setUnderline(9, 8 + underlinedText.length)
        }

        viewModel.user.observe(viewLifecycleOwner) { user ->
            Log.d(LOG_TAG, "User observed: $user")
            binding.helloTv.text = "Welcome back, ${user.name}!"
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
            // FIXME напрямую указываем вьюмодельке
            viewModel.setName(name)
        }
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val LOG_TAG = "HomeFragment"
    }
}