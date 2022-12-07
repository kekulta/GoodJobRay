package ru.kekulta.goodjobray.utils.ModelAccessInterfaces

import ru.kekulta.goodjobray.utils.Day

interface DateModel {
    val actualDay: Int
    val actualMonth: Int
    val actualYear: Int
    val actualHour: Int

    fun getDaysFor(month: Int, year: Int): List<Day>
}