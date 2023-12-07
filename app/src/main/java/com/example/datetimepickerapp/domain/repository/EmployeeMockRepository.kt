package com.example.datetimepickerapp.domain.repository

import com.example.datetimepickerapp.domain.remote.db.EmployeeDao
import com.example.datetimepickerapp.domain.MockAPI
import com.example.datetimepickerapp.models.Employee
import com.google.gson.Gson
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class EmployeeMockRepository(employeeDao: EmployeeDao) : IEmployeeMockRepository {

    private val pattern = "yyyy-MM-dd HH:mm"

    private val mockAPI = MockAPI(employeeDao)
    private var gson = Gson()


    override suspend fun getLatestDateTime(): LocalDateTime {
        val response = mockAPI.makeGetRequest("mock/api/date") ?: throw Exception()

        if (response.isSuccessful) {
            val jsonString = response.body().toString()
            val employee = gson.fromJson(jsonString, Employee::class.java)
            return createLocalDateTimeFromString(employee.dateTime)
        }
        throw Exception("Something went wrong!")
    }


    override suspend fun postDateTime(dateTime: LocalDateTime) {
        val formattedDateTime = formatDateTime(dateTime)
        val employee = Employee(dateTime = formattedDateTime)

        val response = mockAPI.makePostRequest("mock/api/date",employee)

        response?.takeIf { it.isSuccessful }
            ?: throw Exception("Something went wrong!")

    }
    private fun createLocalDateTimeFromString(dateTimeString: String): LocalDateTime {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        return LocalDateTime.parse(dateTimeString, formatter)
    }

    private fun formatDateTime(dateTime: LocalDateTime): String {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        return dateTime.format(formatter)
    }



}
