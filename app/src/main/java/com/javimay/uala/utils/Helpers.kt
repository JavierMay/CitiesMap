package com.javimay.uala.utils

import com.javimay.uala.data.model.City
import com.javimay.uala.data.model.Coord

fun getCitiesListMocked() = listOf(
    City(
        country = "US",
        name = "West Palm Beach",
        id = 1,
        coord = Coord(4.245, -34.454)
    ),
    City(
        country = "US",
        name = "Albuquerque",
        id = 2,
        coord = Coord(4.245, -34.454)
    ),
    City(
        country = "AU",
        name = "Sydney",
        id = 3,
        coord = Coord(4.245, -34.454)
    ),
    City(
        country = "US",
        name = "Arizona",
        id = 4,
        coord = Coord(4.245, -34.454)
    ),
    City(
        country = "US",
        name = "Alabama",
        id = 5,
        coord = Coord(4.245, -34.454),
        isFavorite = true
    ),
)

fun getRandomCity() = getCitiesListMocked().random()