package com.example.datetimetest

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.datetimepickerapp.toOrdinal
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import java.util.*

class CalendarHandler {

    private var _today = LocalDate.now()
    val today : LocalDate get() = _today

    private var _initialWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    private val initialWeek: LocalDate get() = _initialWeek

    private var _startOfWeek by mutableStateOf(today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)))
    val startOfWeek: LocalDate get() = _startOfWeek

    private val endOfWeek : LocalDate get() = _startOfWeek.plusDays(6)

    private var _selectedDay by mutableStateOf(today)
    val selectedDay: LocalDate get() =  _selectedDay



    private val dayString: String get() = selectedDay.dayOfWeek.toString().lowercase()
        .replaceFirstChar { it.uppercase()  }
    private val dayOfMonthString: String get() = selectedDay.dayOfMonth.toOrdinal()
    private val monthString: String get() = selectedDay.month.toString().lowercase()
        .replaceFirstChar { it.uppercase()  }
    private val yearString: String get() = selectedDay.year.toString()






    fun getWeekDays() : List<LocalDate>{
        val weekDates = mutableListOf<LocalDate>()

        var currentDate = startOfWeek

        while (!currentDate.isAfter(endOfWeek)) {
            weekDates.add(currentDate)
            currentDate = currentDate.plusDays(1)
        }

        return weekDates
    }



    fun getWeekNumber(): Int {
        val weekFields = WeekFields.of(Locale.getDefault())

        return startOfWeek.get(weekFields.weekOfWeekBasedYear())
    }

    fun changeSelectedDay(day: LocalDate){
        if(day > today){
            return
        }
        setSelectedDay(day)
    }

    private fun setSelectedDay(date: LocalDate){
        _selectedDay = date
    }

    fun setStartOfWeek(date: LocalDate){
        _startOfWeek = date
    }

    fun isInitialWeek(): Boolean {
        if(initialWeek == _startOfWeek){
            return true
        }
        return false
    }
    fun getSelectedDateAsString(): String {
        return "$dayString $dayOfMonthString, $monthString $yearString"
    }


}