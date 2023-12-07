package com.example.datetimepickerapp.domain.repository

import retrofit2.http.GET
import retrofit2.http.POST
import java.time.LocalDateTime

interface IEmployeeMockRepository {



    @GET("mock/api/date")
    suspend fun getLatestDateTime(): LocalDateTime


    @POST("mock/api/date")
    suspend fun postDateTime(dateTime: LocalDateTime)
}