package ru.kekulta.goodjobray.screens.planner.presentation

import ru.kekulta.simpleviews.widget.CardView

interface DateRecyclerClickListener {
    fun onClick(index: Int, cardView: CardView)
}