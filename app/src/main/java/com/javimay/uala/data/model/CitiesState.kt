package com.javimay.uala.data.model

sealed class CitiesState {
    data class Error(val message: String): CitiesState()
    data object Loading: CitiesState()
    data object Empty: CitiesState()
    data class Success(val cities: List<City>): CitiesState()
}