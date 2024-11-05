package com.javimay.uala.data.repository

import com.javimay.uala.data.api.IUalaService
import com.javimay.uala.data.model.City
import retrofit2.Response
import javax.inject.Inject

class CitiesRemoteDataSourceImpl @Inject constructor(
    private val ualaService: IUalaService
): ICitiesRemoteDataSource {
    override suspend fun getCities(): Response<List<City>> =
        ualaService.getCities()
}