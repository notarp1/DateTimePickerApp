package com.example.datetimepickerapp.dal.db;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.datetimepickerapp.models.Employee;


@androidx.room.Database(entities = {Employee.class}, version = 5)
public abstract class Database extends RoomDatabase {

    public abstract EmployeeDao employeeDao();
    private static volatile Database INSTANCE;

    public static Database getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (Database.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            Database.class, "DateTimeDB"
                    )
                            .fallbackToDestructiveMigration()
                            .build();

                }
            }
        }
        return INSTANCE;
    }




}