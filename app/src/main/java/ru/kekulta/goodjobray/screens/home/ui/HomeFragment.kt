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

        observeState()
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
            activityResultLauncher.let { resultLaucher ->
                requireNotNull(resultLaucher) { "ResultLauncher must be initialized" }
                resultLaucher.launch(galleryIntent)
            }
            true
        }
    }

    private fun observeState() {
        viewModel.observeState().observe(viewLifecycleOwner) { homeScreenState ->
            Log.d(LOG_TAG, "bitmap observed: $homeScreenState")
            homeScreenState.photo?.let {photo ->
                binding.profilePv.bitmap = photo
            }

            homeScreenState.tasksNumber?.let { tasksNum ->
                val underlinedText = "$tasksNum task${if (tasksNum > 1) "s" else ""}"
                val fullText = "You have $underlinedText for today"

                binding.profilePv.text = fullText
                binding.profilePv.setUnderline(9, 8 + underlinedText.length)
            }

            homeScreenState.user?.let { user ->
                Log.d(LOG_TAG, "User observed: $user")
                binding.helloTv.text = "Welcome back, ${user.name}!"

            }

            homeScreenState.progression?.let { progression ->
                val progress: Float = progression / 100f
                binding.progressCb.progress = progress
            }
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