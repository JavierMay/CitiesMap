package com.javimay.uala.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.javimay.uala.data.model.CitiesState
import com.javimay.uala.data.model.City
import com.javimay.uala.domain.usecases.GetCitiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CitiesViewModel @Inject constructor(
    private val getCitiesUseCase: GetCitiesUseCase
): ViewModel() {

    private val _state = MutableStateFlow<CitiesState>(CitiesState.Empty)
    val state = _state.asStateFlow()

    private var citiesList = mutableStateOf<List<City>>(listOf())
    private var cachedCityList = listOf<City>()
    private var isSearchStarting  = true
    var textSearch by mutableStateOf("")
    var searchFavoritesEnabled by mutableStateOf(false)
    var selectedCity by mutableStateOf<City?>(null)
        private set

    fun updateTextSearch(newText: String) {
        textSearch = newText
    }

    fun onSearchFavoritesEnabled(isEnabled: Boolean) {
        searchFavoritesEnabled = isEnabled
    }

    fun getCities() {
        _state.value = CitiesState.Loading
        viewModelScope.launch {
            citiesList.value = getCitiesUseCase.execute()
            _state.value = CitiesState.Success(
                cities = citiesList.value
            )
        }
    }

    fun toggleFavorite(city: City) {
        viewModelScope.launch {
            val index = citiesList.value.indexOf(city)
            if (index == -1) return@launch
            val updatedCities = citiesList.value.toMutableList()
            val updatedCity = citiesList.value[index].copy(isFavorite = !city.isFavorite)
            updatedCities[index] = updatedCity
            citiesList.value = updatedCities
            if (cachedCityList.isNotEmpty() && cachedCityList.contains(city)) {
                val indexCache = cachedCityList.indexOf(city)
                val updatedCitiesCache = cachedCityList.toMutableList()
                updatedCitiesCache[indexCache] = updatedCity
                cachedCityList = updatedCitiesCache
            }
            _state.value = CitiesState.Success(citiesList.value)
        }
    }

    fun selectCity(city: City) {
        selectedCity = city
    }

    fun searchCities(query: String, onlyFavorites: Boolean = false) {
        val listToSearch = if (isSearchStarting) citiesList.value else cachedCityList
        viewModelScope.launch(Dispatchers.Default) {
            if (query.isEmpty()) {
                citiesList.value = cachedCityList.ifEmpty { citiesList.value }
                isSearchStarting = true
                _state.value = CitiesState.Success(citiesList.value)
                return@launch
            }
            val results = listToSearch.filter {
                if (onlyFavorites) {
                    it.name.contains(query.trim(), ignoreCase = true) &&
                            it.isFavorite
                } else {
                    it.name.contains(query.trim(), ignoreCase = true)
                }
            }
            if (isSearchStarting) {
                cachedCityList = citiesList.value
                isSearchStarting = false
            }
            citiesList.value = results
            _state.value = CitiesState.Success(citiesList.value)
        }
    }
}