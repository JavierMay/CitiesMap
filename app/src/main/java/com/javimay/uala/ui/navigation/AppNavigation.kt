package com.javimay.uala.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.javimay.uala.ui.screens.CitiesScreen
import com.javimay.uala.ui.screens.MapScreen
import com.javimay.uala.utils.PARAM_CITY_NAME
import com.javimay.uala.utils.PARAM_LATITUDE
import com.javimay.uala.utils.PARAM_LONGITUDE

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navHostController: NavHostController
) {
    NavHost(navController = navHostController, startDestination = AppScreens.CitiesScreen.route) {
        composable(route = AppScreens.CitiesScreen.route) {
            CitiesScreen(modifier = modifier, navController = navHostController)
        }
        composable(route = AppScreens.MapScreen.route +
                "/{$PARAM_CITY_NAME}/{$PARAM_LATITUDE}/{$PARAM_LONGITUDE}") { backStackEntry ->
            val cityName = backStackEntry.arguments?.getString(PARAM_CITY_NAME)
            val latitude = backStackEntry.arguments?.getString(PARAM_LATITUDE)
            val longitude = backStackEntry.arguments?.getString(PARAM_LONGITUDE)
            if (cityName != null && latitude != null && longitude != null) {
                MapScreen(
                    modifier = modifier,
                    cityName = cityName,
                    latitude = latitude.toDouble(),
                    longitude = longitude.toDouble(),
                    isPortrait = true,
                    navController = navHostController)
            }
        }
    }
}