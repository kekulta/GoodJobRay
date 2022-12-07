package ru.kekulta.goodjobray.data


import ru.kekulta.goodjobray.utils.Day
import ru.kekulta.goodjobray.utils.ModelAccessInterfaces.DateModel
import java.util.*

object DateGlobalModel : DateModel {

    override val actualDay: Int
        get() = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    override val actualMonth: Int
        get() = Calendar.getInstance().get(Calendar.MONTH)
    override val actualYear: Int
        get() = Calendar.getInstance().get(Calendar.YEAR)
    override val actualHour: Int
        get() = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

    override fun getDaysFor(month: Int, year: Int): List<Day> {
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

}