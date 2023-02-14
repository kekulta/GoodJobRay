package ru.kekulta.goodjobray.screens.planner.ui

import android.graphics.Color
import android.os.Bundle
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
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.kekulta.goodjobray.R
import ru.kekulta.goodjobray.activity.data.Task
import ru.kekulta.goodjobray.databinding.FragmentPlannerBinding
import ru.kekulta.goodjobray.screens.planner.presentation.DateRecyclerClickListener
import ru.kekulta.goodjobray.screens.planner.presentation.DatesAdapter
import ru.kekulta.goodjobray.screens.planner.presentation.PlannerViewModel
import ru.kekulta.goodjobray.utils.Day
import ru.kekulta.goodjobray.utils.dp

import ru.kekulta.simpleviews.widget.CardView


const val TIME_ID = 1000000

class PlannerFragment : Fragment() {

    private var _binding: FragmentPlannerBinding? = null
    private val binding get() = _binding!!
    private lateinit var datesRecycler: RecyclerView
    private lateinit var viewModel: PlannerViewModel
    private var timeItems = mutableListOf<TextView>()
    private var taskItems = mutableListOf<CardView>()

    private val adddingLiveData = MediatorLiveData<Pair<Int?, Int?>>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlannerBinding.inflate(inflater, container, false)
        return binding.root

    }

    inner class DatesListener : DateRecyclerClickListener {
        override fun onClick(index: Int, cardView: CardView) {
            if (::viewModel.isInitialized) viewModel.setCurrentDay(index + 1)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(PlannerViewModel::class.java)

        generateTimeline()

        configureAddingLiveData()

        setupDatesRecycler()

        observerCurrentDate()

        bindMonthSwitch()

        bindToolbar()
    }


    private fun bindToolbar() {
        binding.toolbar.addIcon.setOnClickListener {
            askForTask()
        }
    }

    private fun bindMonthSwitch() {
        binding.monthYearTv.setOnClickListener {
            viewModel.nextMonth()
        }
        binding.monthYearTv.setOnLongClickListener {
            viewModel.previousMonth()
            true
        }
    }

    private fun observerCurrentDate() {
        viewModel.tasks.observe(viewLifecycleOwner) { tasks ->
            taskItems.forEach {
                binding.timesContainer.removeView(it)
            }
            generateTask(tasks)
        }

        viewModel.currentMonth.observe(viewLifecycleOwner) { curMonth ->
            viewModel.currentYear.value.let { curYear ->
                val year = curYear ?: "Year"
                binding.monthYearTv.text = "${Day.monthFullName(curMonth)} $year"
            }
        }
    }

    private fun setupDatesRecycler() {
        datesRecycler = binding.datesRv
        datesRecycler.apply {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = DatesAdapter().apply {
                viewModel.days.observe(viewLifecycleOwner) {
                    //println(it)
                    days = it
                }
                viewModel.currentDay.observe(viewLifecycleOwner) {
                    currentDay = it
                }
                listener = DatesListener()
            }
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
                    println("task: $task long clicked")
                    viewModel.deleteTask(task)
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
            binding.tasksSv.smoothScrollTo(0, timeItems[viewModel.currentHour.value!!].top)
        }



    }


    inner class TimeItemListener : View.OnClickListener, View.OnLongClickListener {
        override fun onClick(view: View) {
            println("TimeItem ${view.id - TIME_ID}-${viewModel.currentDay.value}-${viewModel.currentMonth.value}-${viewModel.currentYear.value} clicked")
            addingLiveDataClicked(view.id - TIME_ID)
        }

        override fun onLongClick(view: View): Boolean {
            addTask(view.id - TIME_ID)
            println("TimeItem ${view.id - TIME_ID}-${viewModel.currentDay.value}-${viewModel.currentMonth.value}-${viewModel.currentYear.value} long clicked")
            return true
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
            viewModel.addTask(
                hourFrom, hourTo, title.ifBlank { null }
            )
        }
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    fun addingLiveDataClicked(hour: Int) {
        adddingLiveData.value.let { value ->
            when {
                value == null || (value.first == null && value.second == null) -> adddingLiveData.value =
                    Pair(hour, null)
                value.first == hour -> nullAddingLiveData()
                value.first != null -> {
                    if (hour < (value.first ?: 23)) nullAddingLiveData()
                    else adddingLiveData.value = Pair(value.first, hour)
                }
                else -> nullAddingLiveData()
            }
        }
    }

    private fun configureAddingLiveData() {
        adddingLiveData.addSource(viewModel.currentDay) {
            nullAddingLiveData()
        }
        adddingLiveData.addSource(viewModel.currentMonth) {
            nullAddingLiveData()
        }
        adddingLiveData.addSource(viewModel.currentYear) {
            nullAddingLiveData()
        }

        adddingLiveData.observe(viewLifecycleOwner) {
            val fromHour = it.first
            val toHour = it.second
            if (fromHour != null && toHour != null) {
                nullAddingLiveData()
                println("adding task from $fromHour to $toHour")
                addTask(fromHour, toHour)
            }
        }
    }

    private fun nullAddingLiveData() {
        if (adddingLiveData.value != Pair(null, null)) {
            adddingLiveData.value = Pair(null, null)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}