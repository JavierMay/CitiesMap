package com.javimay.uala.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.javimay.uala.data.model.City
import com.javimay.uala.ui.theme.Gold
import com.javimay.uala.utils.getRandomCity

@Composable
fun CityItem(
    modifier: Modifier = Modifier,
    city: City,
    onCityClicked: () -> Unit = {},
    onFavorite: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .padding(12.dp)
    ) {
        Column(Modifier.clickable(onClick = onCityClicked)) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = modifier,
                    text = city.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    modifier = modifier.padding(start = 12.dp),
                    text = city.country,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Text(
                text = "Lat: ${city.coord.latitude}, Lon: ${city.coord.longitude}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        IconButton(
            onClick = onFavorite,
            modifier = modifier.align(Alignment.CenterEnd)
        ) {
            Icon(
                imageVector = Icons.Outlined.Star,
                contentDescription = "Delete",
                tint = if(city.isFavorite) Gold else Color.DarkGray
            )
        }
    }
}

@Composable
@Preview(uiMode = UI_MODE_NIGHT_YES, name = "Dark mode", showBackground = true)
private fun CityItemPreview() {
    val city = getRandomCity()
    CityItem(city = city)
}