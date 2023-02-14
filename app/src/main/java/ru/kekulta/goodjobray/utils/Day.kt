package ru.kekulta.goodjobray.utils

import java.util.Calendar

data class Day(val dayOfMonth: Int, val monthNum: Int, val dayOfWeek: Int, val year: Int = 2022) {
    companion object {
        val actualDay: Int
            get() = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        val actualMonth: Int
            get() = Calendar.getInstance().get(Calendar.MONTH)
        val actualYear: Int
            get() = Calendar.getInstance().get(Calendar.YEAR)
        val actualHour: Int
            get() = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        fun getDaysFor(month: Int, year: Int): List<Day> {
            val calendar = Calendar.getInstance()

            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            val daysIn = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            val daysList = List<Day>(daysIn) {
                calendar.set(Calendar.DAY_OF_MONTH, it + 1)
                Day(it + 1, month, calendar.get(Calendar.DAY_OF_WEEK), year)
            }
            return daysList
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
