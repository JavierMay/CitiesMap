package com.javimay.uala.data.repository

import com.javimay.uala.data.model.City
import retrofit2.Response

interface ICitiesRemoteDataSource {

    suspend fun getCities(): Response<List<City>>
}