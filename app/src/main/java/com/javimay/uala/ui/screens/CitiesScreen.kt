package com.javimay.uala.ui.screens

import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.javimay.uala.data.model.CitiesState
import com.javimay.uala.data.model.City
import com.javimay.uala.ui.components.CityItem
import com.javimay.uala.ui.components.SearchBar
import com.javimay.uala.ui.navigation.AppScreens
import com.javimay.uala.ui.theme.LightBlue
import com.javimay.uala.utils.getCitiesListMocked

@Composable
fun CitiesScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val localConfig = LocalConfiguration.current
    val isPortrait = remember {
        localConfig.orientation == Configuration.ORIENTATION_PORTRAIT
    }
    Box (
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        GetCities(
            modifier = modifier,
            navController = navController,
            isPortrait = isPortrait
        )
    }
}

@Composable
fun GetCities(
    modifier: Modifier = Modifier,
    viewModel: CitiesViewModel = hiltViewModel(),
    navController: NavController,
    isPortrait: Boolean = true
) {
    val state = viewModel.state.collectAsState()
    when (state.value) {
        is CitiesState.Empty -> {
            viewModel.getCities()
        }
        is CitiesState.Loading -> {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary
            )
        }
        is CitiesState.Success -> {
            val citiesList = (state.value as CitiesState.Success).cities
            if (isPortrait) {
                PortraitScreen(
                    modifier = modifier,
                    viewModel = viewModel,
                    citiesList = citiesList,
                    navController = navController
                )
            } else {
                LandscapeScreen(
                    modifier = modifier,
                    viewModel = viewModel,
                    citiesList = citiesList,
                    navController = navController
                )
            }
        }
        else -> {}
    }
}

@Composable
fun PortraitScreen(
    modifier: Modifier = Modifier,
    viewModel: CitiesViewModel = hiltViewModel(),
    citiesList: List<City>,
    navController: NavController = rememberNavController(),
    selectedCity: City? = null,
    isPortrait: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchBar(
            modifier = Modifier.padding(8.dp),
            textSearch = viewModel.textSearch,
            onUpdateText = viewModel::updateTextSearch,
            onSearch = viewModel::searchCities,
            onUpdateFavorite = viewModel::onSearchFavoritesEnabled,
            isFavorite = viewModel.searchFavoritesEnabled
        )
        Spacer(modifier = Modifier.padding(vertical = 4.dp))
        LazyColumn {
            itemsIndexed(items = citiesList) { index, city ->
                var backgroundColor = if (index % 2 == 0) Color.White else Color.LightGray

                if (!isPortrait && city == selectedCity) {
                    backgroundColor = LightBlue
                }
                CityItem(
                    modifier = Modifier
                        .background(color = backgroundColor),
                    city = city,
                    onCityClicked = {
                        if (isPortrait) {
                            navController.navigate(
                                AppScreens.MapScreen.route
                                        + "/${city.name}/" + city.coord.latitude + "/" + city.coord.longitude
                            )
                        } else {
                            backgroundColor = LightBlue
                            viewModel.selectCity(city)
                        }
                    },
                    onFavorite = { viewModel.toggleFavorite(city) }
                )
            }
        }
    }
}

@Composable
@Preview(uiMode = UI_MODE_NIGHT_YES, name = "Dark mode", showBackground = true)
private fun PortraitScreenPreview() {
    PortraitScreen(
        citiesList = getCitiesListMocked()
    )
}

@Composable
fun LandscapeScreen(
    modifier: Modifier = Modifier,
    viewModel: CitiesViewModel = hiltViewModel(),
    citiesList: List<City>,
    navController: NavController = rememberNavController(),
) {
    val selectCity = viewModel.selectedCity
    val selectedCity = selectCity ?: citiesList[0]
    Row {
        PortraitScreen(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.8f),
            viewModel = viewModel,
            citiesList = citiesList,
            selectedCity = selectedCity,
            isPortrait = false
        )
        MapScreen(modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
            cityName = selectedCity.name,
            latitude = selectedCity.coord.latitude,
            longitude = selectedCity.coord.longitude,
            isPortrait = false,
            navController = navController
        )
    }
}

@Composable
@Preview(uiMode = UI_MODE_NIGHT_YES, name = "Dark mode", showBackground = true,
    device = "spec:parent=pixel_5,orientation=landscape"
)
private fun LandscapeScreenPreview() {
    LandscapeScreen(
        citiesList = getCitiesListMocked()
    )
}