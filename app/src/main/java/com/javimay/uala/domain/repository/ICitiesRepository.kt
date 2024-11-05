package com.javimay.uala.domain.repository

import com.javimay.uala.data.model.City

interface ICitiesRepository {

    suspend fun getCities(): List<City>
}