package com.example.datetimepickerapp.views

import android.database.sqlite.SQLiteConstraintException
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datetimepickerapp.DatePickerHandler
import com.example.datetimepickerapp.domain.repository.IEmployeeMockRepository
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.Exception


class DateTimeMainActivityViewModel(private val employeeRepo: IEmployeeMockRepository) :
    ViewModel() {

    sealed class ViewState {
        data class Success(val dateTime: LocalDateTime) : ViewState()
        data class Error(val errorType: ErrorType, val errorMessage: String) : ViewState()
        data class Loading(val status: Boolean) : ViewState()
    }


    private val datePickerHandler: DatePickerHandler = DatePickerHandler()


    private var _selectedTime by mutableStateOf(getLocalTime())
    val selectedTime: LocalTime get() = _selectedTime
    val currentDate: LocalDate get() = datePickerHandler.currentDate
    val selectedDate: LocalDate get() = datePickerHandler.selectedDate


    private var _viewState by mutableStateOf<ViewState>(ViewState.Success(selectedDate.atTime(selectedTime)))

    val viewState: ViewState get() = _viewState

    init {
        viewModelScope.launch {
            getLatestDateTime()
        }
    }



    private suspend fun getLatestDateTime() {
        try {
            employeeRepo.getLatestDateTime()
                .collect { dateTime ->
                    _viewState = ViewState.Success(dateTime)
                    initializeDateTime(dateTime)
                }
        } catch (e: Exception) {
            _viewState = ViewState.Error(ErrorType.UnexpectedError,"Error fetching latest date and time.")
        }
    }



    suspend fun postDateTime()  {
        setViewStateLoading()

        viewModelScope.launch {
            try {
                if (selectedTime > getLocalTime()) {
                    setSelectedTime(getLocalTime())
                    throw UnsupportedOperationException()
                }
                val dateTime = selectedDate.atTime(selectedTime)
                employeeRepo.postDateTime(dateTime)

                setSuccessViewStateDateTime(dateTime)

            } catch (e: SQLiteConstraintException) {
                _viewState = ViewState.Error(ErrorType.SQLiteConstraint, e.message ?: "")
            } catch (e: UnsupportedOperationException) {
                _viewState = ViewState.Error(ErrorType.UnsupportedOperationException, e.message ?: "")
            } catch (e: Exception) {
                _viewState = ViewState.Error(ErrorType.UnsupportedOperationException, e.message ?: "")
            }
        }
    }

    private fun setSuccessViewStateDateTime(dateTime: LocalDateTime){
        _viewState = ViewState.Success(dateTime)
    }

    private fun setViewStateLoading(){
        _viewState = ViewState.Loading(true)
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



}


sealed class ErrorType {
    object SQLiteConstraint : ErrorType()
    object UnsupportedOperationException : ErrorType()
    object UnexpectedError : ErrorType()
}