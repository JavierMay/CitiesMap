package com.javimay.uala.data.repository

import com.javimay.uala.data.model.City
import com.javimay.uala.domain.repository.ICitiesRepository
import retrofit2.Response
import javax.inject.Inject

class CitiesRepositoryImpl @Inject constructor(
    private val citiesRemoteDataSource: ICitiesRemoteDataSource
): ICitiesRepository {
    override suspend fun getCities(): List<City> = getCitiesFromApi()

    private suspend fun getCitiesFromApi(): List<City> {
        val response: Response<List<City>> = citiesRemoteDataSource.getCities()
        response.body()?.let { cities ->
            return cities.sortedBy { it.name }
        }
        return emptyList()
    }
}