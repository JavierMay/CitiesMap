package com.javimay.uala.data.model

import com.google.gson.annotations.SerializedName

data class City(
    val country: String,
    val name: String,
    @SerializedName("_id")
    val id: Long,
    val coord: Coord,
    var isFavorite: Boolean = false,
) /*{
    companion object {
        fun empty(): City =
            City(
                country = "",
                name = "",
                id = 0,
                coord = Coord(0.0, 0.0)
            )
    }
}*/
