package com.example.datetimepickerapp

import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import com.example.datetimetest.CalendarHandler
import java.time.LocalDate

class DateTimeMainActivityViewModel() : ViewModel() {

    private val calendar: CalendarHandler = CalendarHandler()

    val selectedDay: LocalDate get() = calendar.selectedDay
    val today : LocalDate get() =  calendar.today



    fun goToPreviousWeek(){
        val prevWeek = calendar.startOfWeek.minusDays(7)
        calendar.setStartOfWeek(prevWeek)
    }

    fun goToNextWeek(){
        val nextWeek = calendar.startOfWeek.plusDays(7)
        calendar.setStartOfWeek(nextWeek)

    }

    fun getWeekNumber(): Int {
        return calendar.getWeekNumber();
    }
    fun getWeekDays() : List<LocalDate>{
        return  calendar.getWeekDays()
    }

    fun changeSelectedDay(day: LocalDate){
        return calendar.changeSelectedDay(day)
    }

    fun isInitialWeek(): Boolean {
        return calendar.isInitialWeek()
    }
    fun getSelectedDateAsString(): String {
        return calendar.getSelectedDateAsString()
    }

}