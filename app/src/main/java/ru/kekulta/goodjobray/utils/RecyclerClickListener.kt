package ru.kekulta.goodjobray.screens.planner

import android.view.View
import ru.kekulta.goodjobray.data.Note
import ru.kekulta.simpleviews.widget.CardView
import ru.kekulta.simpleviews.widget.TextCard

interface DateRecyclerClickListener {
    fun onClick(index: Int, cardView: CardView)
}

interface NoteRecyclerClickListener {
    fun onClick(index: Int, note: Note)
    fun onLongClick(index: Int, note: Note): Boolean
}