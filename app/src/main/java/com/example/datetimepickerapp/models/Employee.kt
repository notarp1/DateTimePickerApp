package com.example.datetimepickerapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "employees")
data class Employee(
    @PrimaryKey
    @ColumnInfo(name = "check_in_date")
    val dateTime: String
)