package com.example.datetimepickerapp.domain.repository

import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.POST
import java.time.LocalDateTime

interface IEmployeeMockRepository {



    @GET("mock/api/date")
    suspend fun getLatestDateTime(): Flow<LocalDateTime>


    @POST("mock/api/date")
    suspend fun postDateTime(dateTime: LocalDateTime)
}