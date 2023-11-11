package com.example.datetimepickerapp.dal.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.datetimepickerapp.models.Employee

@Dao
interface EmployeeDao {
    @Query("SELECT * FROM employees ORDER BY check_in_date DESC")
    suspend fun getLatestEmployee(): Employee?


    @Insert
    suspend fun postEmployee(employee: Employee)
}