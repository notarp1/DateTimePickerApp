package com.example.datetimepickerapp.domain

import com.example.datetimepickerapp.domain.remote.mock_backend.EmployeeDao
import com.example.datetimepickerapp.models.Employee
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.Response
import java.time.LocalDate

class MockAPI(private val employeeDao: EmployeeDao) {
    private var gson = Gson()
    suspend fun makePostRequest(path: String, obj: Employee): Response<ResponseBody>? {

        return when (path) {
            "mock/api/date" -> {
                employeeDao.postEmployee(obj)
                Response.success(
                    201,
                    ResponseBody.create(MediaType.parse("application/json"), "ok")
                )
            }

            else ->
                Response.error(
                    404,
                    ResponseBody.create(MediaType.parse("application/json"), "Not Found")
                )
        }
    }


    suspend fun makeGetRequest(path: String): Response<String>? {

        return when (path) {
            "mock/api/date" -> {
                var employee = employeeDao.getLatestEmployee()

                if(employee == null){
                   employee = Employee(defaultDate())
                }
                val jsonString = gson.toJson(employee)

                Response.success(200, jsonString)

            }

            else ->
                Response.error(
                    404,
                    ResponseBody.create(MediaType.parse("application/json"), "Not Found")
                )
        }
    }

    private fun defaultDate(): String {
        val date = LocalDate.now().toString()
        return "$date 06:30"
    }

}
