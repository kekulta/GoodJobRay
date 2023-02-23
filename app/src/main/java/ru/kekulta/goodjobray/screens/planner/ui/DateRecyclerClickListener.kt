package ru.kekulta.goodjobray.screens.planner.ui

import ru.kekulta.simpleviews.widget.CardView

interface DateRecyclerClickListener {
    fun onClick(index: Int, cardView: CardView)
}