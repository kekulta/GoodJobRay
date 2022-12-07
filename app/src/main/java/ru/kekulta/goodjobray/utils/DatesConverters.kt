package ru.kekulta.goodjobray.utils

fun threeLetterDayOfWeek(dayNum: Int): String {
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

fun monthFull(monthNum: Int): String {
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