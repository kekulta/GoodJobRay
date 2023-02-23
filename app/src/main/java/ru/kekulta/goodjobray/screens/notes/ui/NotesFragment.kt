package ru.kekulta.goodjobray.screens.notes.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import ru.kekulta.goodjobray.R
import ru.kekulta.goodjobray.activity.data.Note
import ru.kekulta.goodjobray.databinding.FragmentNotesBinding
import ru.kekulta.goodjobray.di.DI
import ru.kekulta.goodjobray.screens.notes.presentation.NotesViewModel
import ru.kekulta.goodjobray.screens.planner.presentation.PlannerViewModel


class NotesFragment : Fragment() {


    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!
    private lateinit var notesRecycler: RecyclerView
    private lateinit var pinnedNotesRecycler: RecyclerView
    private val viewModel: NotesViewModel by viewModels({ requireActivity() }) { NotesViewModel.Factory }
    private val notesAdapter = NotesAdapter().apply {
        listener = OnClickListener()
    }
    private val pinnedNotesAdapter = NotesAdapter().apply {
        listener = OnClickListener()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()
        setupNotesRecycler()
        setupPinnedNotesRecycler()
        bindToolbar()
    }

    private fun observeState() {
        viewModel.observeState().observe(viewLifecycleOwner) { state ->
            state.notes?.let { notes ->
                notesAdapter.notes = notes
            }

            state.pinnedNotes?.let { notes ->
                pinnedNotesAdapter.notes = notes
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bindToolbar() {
        binding.toolbar.addIcon.setOnClickListener {
            askForNote()
        }
    }

    private fun setupPinnedNotesRecycler() {
        pinnedNotesRecycler = binding.pinnedNotesRv
        pinnedNotesRecycler.apply {
            adapter = pinnedNotesAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }
    }

    private fun setupNotesRecycler() {
        notesRecycler = binding.otherNotesRv
        notesRecycler.apply {
            adapter = notesAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        }

    }

    private fun askForNote() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Take a note!")


        val dialogView = layoutInflater.inflate(R.layout.view_dialog_note, null)
        builder.setView(dialogView)



        builder.setPositiveButton(
            "OK"
        ) { dialog, _ ->
            val title = dialogView.findViewById<EditText>(R.id.titleEt).text.toString()
            val text = dialogView.findViewById<EditText>(R.id.textEt).text.toString()



            viewModel.addNote(title, text)


            dialog.cancel()
        }
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    inner class OnClickListener : NoteRecyclerClickListener {
        override fun onClick(index: Int, note: Note) {
            println("note: $note clicked")
            viewModel.changePin(note)
        }

        override fun onLongClick(index: Int, note: Note): Boolean {
            println("note: $note long clicked")
            viewModel.deleteNote(note)
            return true
        }
    }

    companion object {
        const val LOG_TAG = "PlannerViewModel"

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                return PlannerViewModel(
                    DI.getTaskRepository()
                ) as T
            }
        }
    }
}