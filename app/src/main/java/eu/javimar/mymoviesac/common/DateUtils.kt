package eu.javimar.mymoviesac.common

import java.time.LocalDate
import java.time.format.DateTimeFormatter


fun getTodayFormattedForQuery(date: LocalDate) : String
{
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return date.format(formatter)
}

fun getOneMonthBefore() : String
{
    val today = LocalDate.now()
    return getTodayFormattedForQuery(today.minusDays(30))
}
