package ru.kekulta.goodjobray.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Calendar

data class Date(val dayOfMonth: Int, val month: Int, val year: Int = 2022) {
    val dayOfWeek: Int
    val daysInMonth: Int

    init {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    fun setDay(day: Int) = Date(day, month, year)
    fun setMonth(month: Int) = Date(dayOfMonth, month, year)
    fun setYear(year: Int) = Date(dayOfMonth, month, year)

    companion object {
        val actualDayOfMonth: Int
            get() = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        val actualMonth: Int
            get() = Calendar.getInstance().get(Calendar.MONTH)
        val actualYear: Int
            get() = Calendar.getInstance().get(Calendar.YEAR)
        val actualHour: Int
            get() = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        fun getDaysFor(month: Int, year: Int): List<Date> {
            val daysIn = getDaysCountFor(month, year)
            val daysList = List(daysIn) {
                Date(it + 1, month, year)
            }
            return daysList
        }

        fun getDaysCountFor(month: Int, year: Int): Int {
            val calendar = Calendar.getInstance()

            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        }

        fun today(): Date {
            val calendar = Calendar.getInstance()
            return Date(
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.YEAR)
            )
        }

        fun threeLetterNameDayOfWeek(dayNum: Int): String {
            return when (dayNum) {
                1 -> "Sun"
                2 -> "Mon"
                3 -> "Tue"
                4 -> "Wen"
                5 -> "Thu"
                6 -> "Fri"
                7 -> "Sat"
                else -> throw java.lang.IllegalArgumentException()
            }
        }

        fun monthFullName(monthNum: Int): String {
            return when (monthNum) {
                0 -> "January"
                1 -> "February"
                2 -> "March"
                3 -> "April"
                4 -> "May"
                5 -> "June"
                6 -> "July"
                7 -> "August"
                8 -> "September"
                9 -> "October"
                10 -> "November"
                11 -> "December"
                else -> throw java.lang.IllegalArgumentException()
            }
        }
    }
}
