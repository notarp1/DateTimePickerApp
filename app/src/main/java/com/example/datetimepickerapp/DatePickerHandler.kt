package com.example.datetimepickerapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import java.util.*

class DatePickerHandler {

    private var _currentDate = LocalDate.now()
    val currentDate : LocalDate get() = _currentDate



    private var _initialWeek = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    private val initialWeek: LocalDate get() = _initialWeek

    private var _startOfWeek by mutableStateOf(currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)))
    val startOfWeek: LocalDate get() = _startOfWeek

    private val endOfWeek : LocalDate get() = _startOfWeek.plusDays(6)

    private var _selectedDate by mutableStateOf(currentDate)
    val selectedDate: LocalDate get() =  _selectedDate




    private val dayString: String get() = selectedDate.dayOfWeek.toString().lowercase()
        .replaceFirstChar { it.uppercase()  }
    private val dayOfMonthString: String get() = selectedDate.dayOfMonth.toOrdinal()
    private val monthString: String get() = selectedDate.month.toString().lowercase()
        .replaceFirstChar { it.uppercase()  }
    private val yearString: String get() = selectedDate.year.toString()





    fun getWeekDates() : List<LocalDate>{
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
        if(day > currentDate){
            return
        }
        setSelectedDay(day)
    }

    private fun setSelectedDay(date: LocalDate){
        _selectedDate = date
    }

    fun setStartOfWeek(date: LocalDate){
        _startOfWeek = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

    }

    fun isInitialWeek(): Boolean {
        if(initialWeek == _startOfWeek){
            return true
        }
        return false
    }
    fun getSelectedDateAsString(time: String): String {
        return "$dayString $dayOfMonthString, $time\n$monthString $yearString"
    }


}