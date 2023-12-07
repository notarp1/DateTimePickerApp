package com.example.datetimepickerapp.domain.remote.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.datetimepickerapp.models.Employee

@Dao
interface EmployeeDao {
    @Query("SELECT * FROM employees ORDER BY check_in_date DESC LIMIT 1")
    suspend fun getLatestEmployee(): Employee?


    @Insert
    suspend fun postEmployee(employee: Employee)
}