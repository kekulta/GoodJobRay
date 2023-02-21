package ru.kekulta.goodjobray.screens.planner.ui

import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.kekulta.goodjobray.R
import ru.kekulta.goodjobray.activity.data.Task
import ru.kekulta.goodjobray.databinding.FragmentPlannerBinding
import ru.kekulta.goodjobray.screens.planner.presentation.DateRecyclerClickListener
import ru.kekulta.goodjobray.screens.planner.presentation.DatesAdapter
import ru.kekulta.goodjobray.screens.planner.presentation.PlannerViewModel
import ru.kekulta.goodjobray.shared.data.utils.dp
import ru.kekulta.goodjobray.utils.Date

import ru.kekulta.simpleviews.widget.CardView


class PlannerFragment : Fragment() {
    companion object {
        const val TIME_ID = 1000000
        const val LOG_TAG = "PlannerFragment"
    }

    private var _binding: FragmentPlannerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlannerViewModel by viewModels({ requireActivity() }) { PlannerViewModel.Factory }
    private var timeItems = mutableListOf<TextView>()
    private var taskItems = mutableListOf<CardView>()
    private val datesAdapter = DatesAdapter().apply {
        listener = DatesListener()
    }
    private lateinit var datesRecycler: RecyclerView

    private val taskCreationBuffer = MutableLiveData<Pair<Int?, Int?>>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlannerBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()

        generateTimeline()

        configureTaskCreationBuffer()

        setupDatesRecycler()

        bindMonthSwitch()

        bindToolbar()
    }

    override fun onPause() {
        super.onPause()
        viewModel.tasksScrollState = binding.tasksSv.scrollY
        viewModel.datesRecyclerState = datesRecycler.layoutManager?.onSaveInstanceState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeState() {
        viewModel.observeState().observe(viewLifecycleOwner) { state ->
            state.tasks?.let { tasks ->
                taskItems.forEach {
                    binding.timesContainer.removeView(it)
                }
                generateTask(tasks)
            }

            state.currentDate?.let { date ->
                val year = date.year
                val monthName = Date.monthFullName(date.month)
                binding.monthYearTv.text = "$monthName $year"

                datesAdapter.currentDay = date.dayOfMonth
                nullTasksCreationBuffer()
            }

            state.days?.let { dates ->
                datesAdapter.dates = dates
            }
        }
    }

    private fun bindToolbar() {
        binding.toolbar.addIcon.setOnClickListener {
            askForTask()
        }
    }

    private fun bindMonthSwitch() {
        binding.monthYearTv.setOnClickListener {
            viewModel.nextMonthButtonClicked()
        }
        binding.monthYearTv.setOnLongClickListener {
            viewModel.previousMonthButtonClicked()
            true
        }
    }

    private fun setupDatesRecycler() {
        datesRecycler = binding.datesRv
        datesRecycler.apply {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = datesAdapter
        }
        if (viewModel.datesRecyclerState == null) {
            datesRecycler.post {
                datesRecycler.smoothScrollToPosition(Date.actualDayOfMonth - 1)
            }
        }
        viewModel.datesRecyclerState?.let {
            datesRecycler.layoutManager?.onRestoreInstanceState(it)
            viewModel.datesRecyclerState = null
        }
    }

    private fun generateTask(tasks: List<Task>) {
        tasks.sortedBy { it.fromHour }.forEachIndexed { index, task ->
            val taskView = CardView(requireContext()).apply {
                startColor = ResourcesCompat.getColor(
                    resources,
                    ru.kekulta.simpleviews.R.color.light_orange,
                    null
                )
                endColor = ResourcesCompat.getColor(
                    resources,
                    ru.kekulta.simpleviews.R.color.light_violet_semitransparent,
                    null
                )
                gradientEndX = 0.5f
                gradientEndY = 1f
                gradientStartX = 0.5f
                gradientStartY = 0f


                absoluteTextSize = 16f.dp

                topText = task.title

                cornerRadius = 16f.dp
                layoutParams = ConstraintLayout.LayoutParams(0, 0).apply {
                    matchConstraintDefaultHeight
                    topToTop = timeItems[task.fromHour].id
                    bottomToBottom = timeItems[task.toHour].id
                    startToEnd = timeItems[0].id
                    endToEnd = binding.timesContainer.id
                    setMargins(
                        0, 0, 8.dp.toInt() * index, 0
                    )
                }
                setOnLongClickListener {
                    viewModel.taskDeleteButtonClicked(task)
                    true
                }
            }
            binding.timesContainer.addView(taskView)
            taskItems.add(taskView)
        }
    }

    private fun generateTimeline() {
        val margin = 16.dp.toInt()
        repeat(24) {
            val textView = TextView(activity).apply {
                id = TIME_ID + it
                layoutParams = ConstraintLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    if (it == 0) topToTop = binding.timesContainer.id
                    else topToBottom = timeItems[it - 1].id
                    setPadding(margin, margin / 2, margin, margin / 2)

                }
                text = "$it:00"
                setTextColor(Color.BLACK)
                textSize = 16.0f
                typeface = ResourcesCompat.getFont(context, ru.kekulta.simpleviews.R.font.nunito)
                setOnClickListener(TimeItemListener())
                setOnLongClickListener(TimeItemListener())
            }

            binding.timesContainer.addView(textView)
            timeItems.add(textView)
        }
        binding.tasksSv.post {
            viewModel.tasksScrollState.let {
                if (it == null) {
                    binding.tasksSv.smoothScrollTo(0, timeItems[Date.actualHour].top)
                } else {
                    binding.tasksSv.scrollY = it
                }
            }

        }
    }

    private fun askForTask() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Set up your task")


        val dialogView = layoutInflater.inflate(R.layout.view_dialog_task, null)
        builder.setView(dialogView)



        builder.setPositiveButton(
            "OK"
        ) { dialog, _ ->
            val title = dialogView.findViewById<EditText>(R.id.titleEt).text.toString()
            val hourFrom =
                dialogView.findViewById<EditText>(R.id.fromHourEt).text.toString().toIntOrNull()
            val hourTo =
                dialogView.findViewById<EditText>(R.id.toHourEt).text.toString().toIntOrNull()

            if (hourFrom != null && hourTo != null && hourTo >= hourFrom && hourTo in 0..23 && hourFrom in 0..23) {
                // FIXME отдаём команду напрямую вьюмодели
                viewModel.addTask(
                    hourFrom, hourTo, title.ifBlank { null }
                )
            }

            dialog.cancel()
        }
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, _ -> dialog.cancel() }

        builder.show()
    }


    private fun addTask(hourFrom: Int, hourTo: Int = hourFrom) {


        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Title of the task")

        val input = EditText(requireContext())
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton(
            "OK"
        ) { _, _ ->
            val title = input.text.toString()
            // FIXME отдаём команду напрямую вьюмодели
            viewModel.addTask(
                hourFrom, hourTo, title.ifBlank { null }
            )
        }
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    fun timeElementClicked(hour: Int) {
        taskCreationBuffer.value.let { value ->
            when {
                value == null || (value.first == null && value.second == null) -> taskCreationBuffer.value =
                    Pair(hour, null)

                value.first == hour -> nullTasksCreationBuffer()
                value.first != null -> {
                    if (hour < (value.first ?: 23)) nullTasksCreationBuffer()
                    else taskCreationBuffer.value = Pair(value.first, hour)
                }

                else -> nullTasksCreationBuffer()
            }
        }
    }

    private fun configureTaskCreationBuffer() {

        taskCreationBuffer.observe(viewLifecycleOwner) {
            val fromHour = it.first
            val toHour = it.second
            if (fromHour != null && toHour != null) {
                nullTasksCreationBuffer()
                println("adding task from $fromHour to $toHour")
                addTask(fromHour, toHour)
            }
        }
    }

    private fun nullTasksCreationBuffer() {
        if (taskCreationBuffer.value != Pair(null, null)) {
            taskCreationBuffer.value = Pair(null, null)
        }
    }


    inner class DatesListener : DateRecyclerClickListener {
        override fun onClick(index: Int, cardView: CardView) {
            viewModel.dayClicked(index + 1)
        }
    }

    inner class TimeItemListener : View.OnClickListener, View.OnLongClickListener {
        override fun onClick(view: View) {
            timeElementClicked(view.id - TIME_ID)
        }

        override fun onLongClick(view: View): Boolean {
            addTask(view.id - TIME_ID)
            return true
        }

    }
}