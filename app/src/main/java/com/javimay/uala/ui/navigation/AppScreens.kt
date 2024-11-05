package com.javimay.uala.ui.navigation

sealed class AppScreens(val route: String) {
    data object CitiesScreen: AppScreens("cities_screen")
    data object MapScreen: AppScreens("map_screen")
}