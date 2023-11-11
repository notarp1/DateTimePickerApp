package com.example.datetimepickerapp.views

import android.database.sqlite.SQLiteConstraintException
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datetimepickerapp.DatePickerHandler
import com.example.datetimepickerapp.dal.repository.IEmployeeMockRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.Exception

class DateTimeMainActivityViewModel(private val employeeRepo: IEmployeeMockRepository) :
    ViewModel() {

    private val datePickerHandler: DatePickerHandler = DatePickerHandler()


    private var _selectedTime by mutableStateOf(getLocalTime())
    val selectedTime: LocalTime get() = _selectedTime
    val currentDate: LocalDate get() = datePickerHandler.currentDate
    val selectedDate: LocalDate get() = datePickerHandler.selectedDate


    private var _showErrorMessageSQLConstraint: Boolean by mutableStateOf(false)
    val showErrorMessageSQLConstraint: Boolean get() = _showErrorMessageSQLConstraint

    private var _showUnsupportedOperationException: Boolean by mutableStateOf(false)
    val showUnsupportedOperationException: Boolean get() = _showUnsupportedOperationException

    init {
        viewModelScope.launch {
            getLatestDateTime()
        }
    }

    private suspend fun getLatestDateTime() {
        try {
            val dateTime = employeeRepo.getLatestDateTime()
            initializeDateTime(dateTime)
        } catch (e: Exception) {
            print("Error")
        }
    }

    suspend fun postDateTime() {
        viewModelScope.launch {
            try {
                if (selectedTime > getLocalTime()) {
                    setSelectedTime(getLocalTime())
                    throw UnsupportedOperationException()
                }
                val dateTime = selectedDate.atTime(selectedTime)
                employeeRepo.postDateTime(dateTime)

            } catch (e: SQLiteConstraintException) {
                _showErrorMessageSQLConstraint = true
            } catch (e: UnsupportedOperationException) {
                _showUnsupportedOperationException = true
            } catch (e: Exception) {
                print(e)
            }
        }
    }

    private fun initializeDateTime(dateTime: LocalDateTime) {
        datePickerHandler.changeSelectedDay(dateTime.toLocalDate())
        datePickerHandler.setStartOfWeek(dateTime.toLocalDate())
        setSelectedTime(dateTime.toLocalTime())

    }


    /* Date Picker */
    fun isInitialWeek(): Boolean {
        return datePickerHandler.isInitialWeek()
    }

    fun goToNextWeek() {
        val nextWeek = datePickerHandler.startOfWeek.plusDays(7)
        datePickerHandler.setStartOfWeek(nextWeek)
    }

    fun goToPreviousWeek() {
        val prevWeek = datePickerHandler.startOfWeek.minusDays(7)
        datePickerHandler.setStartOfWeek(prevWeek)

    }

    fun getWeekNumber(): Int {
        return datePickerHandler.getWeekNumber()
    }

    fun getWeekDates(): List<LocalDate> {
        return datePickerHandler.getWeekDates()
    }

    fun changeSelectedDay(day: LocalDate) {
        return datePickerHandler.changeSelectedDay(day)
    }

    fun getSelectedDateAsString(): String {
        return datePickerHandler.getSelectedDateAsString(selectedTime.toString())
    }


    /* Time Picker */
    fun setTimeByHourMinutes(selectedHour: Int, selectedMinute: Int) {
        setSelectedTime(selectedTime.withHour(selectedHour).withMinute(selectedMinute))
    }

    private fun setSelectedTime(localTime: LocalTime) {
        _selectedTime = localTime
    }

    private fun getLocalTime(): LocalTime {
        return LocalTime.now().withSecond(0).withNano(0)
    }

    /* Error Handling */
    fun setSQLiteConstraintException(bool: Boolean) {
        _showErrorMessageSQLConstraint = bool
    }

    fun setUnsupportedOperationException(bool: Boolean) {
        _showUnsupportedOperationException = bool

    }

}