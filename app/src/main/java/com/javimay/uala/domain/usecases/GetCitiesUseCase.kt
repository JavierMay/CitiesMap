package com.javimay.uala.domain.usecases

import com.javimay.uala.data.model.City
import com.javimay.uala.domain.repository.ICitiesRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class GetCitiesUseCase @Inject constructor(
    private val citiesRepository: ICitiesRepository
) {

    private val citiesListCache: MutableList<City> = mutableListOf()
    private var citiesList: List<City> = listOf()

    suspend fun execute(): List<City> /*{*/ = citiesRepository.getCities()
        /*if (citiesListCache.isEmpty()) {
                citiesListCache.addAll(citiesRepository.getCities() )
        }
        val nextTotalCities = citiesList.size + 1000
        citiesList = citiesListCache.take(nextTotalCities)
        return citiesList
    }*/

}