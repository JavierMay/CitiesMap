package com.javimay.uala.data.api

import com.javimay.uala.data.model.City
import com.javimay.uala.utils.GET_CITIES
import retrofit2.Response
import retrofit2.http.GET

interface IUalaService {

    @GET(GET_CITIES)
    suspend fun getCities(): Response<List<City>>
}