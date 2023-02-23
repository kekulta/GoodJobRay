package ru.kekulta.goodjobray.screens.planner.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import ru.kekulta.goodjobray.R
import ru.kekulta.goodjobray.utils.Date
import ru.kekulta.simpleviews.widget.CardView

class DatesAdapter : RecyclerView.Adapter<DatesAdapter.DateViewHolder>() {
    var currentDay = -1
        set(day) {
            val value = day - 1
            if (value == field) return
            val oldValue = field
            field = value
            notifyItemChanged(oldValue)
            if (value in dates.indices)
                notifyItemChanged(value)
        }
    var listener: DateRecyclerClickListener? = null
    var dates: List<Date> = mutableListOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            //TODO: implement DiffUtil
            notifyDataSetChanged()
        }

    inner class DateViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private val card: CardView = view.findViewById(R.id.dateCv)

        init {
            card.setOnClickListener(this)
        }

        fun onBind() {
            card.apply {
                bottomText = dates[adapterPosition].dayOfMonth.toString()
                topText = Date.threeLetterNameDayOfWeek(dates[adapterPosition].dayOfWeek)
                if (adapterPosition == currentDay) {
                    startColor = ResourcesCompat.getColor(
                        resources,
                        ru.kekulta.simpleviews.R.color.light_orange,
                        null
                    )
                    endColor = ResourcesCompat.getColor(
                        resources,
                        ru.kekulta.simpleviews.R.color.light_violet,
                        null
                    )
                } else {
                    startColor = ResourcesCompat.getColor(
                        resources,
                        ru.kekulta.simpleviews.R.color.transparent,
                        null
                    )
                    endColor = ResourcesCompat.getColor(
                        resources,
                        ru.kekulta.simpleviews.R.color.transparent,
                        null
                    )
                }
                invalidate()
            }

        }

        override fun onClick(view: View?) {
            listener?.onClick(adapterPosition, card)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.view_date, parent, false)
        return DateViewHolder(v)
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        holder.onBind()
    }

    override fun getItemCount(): Int {
        return dates.size
    }
}