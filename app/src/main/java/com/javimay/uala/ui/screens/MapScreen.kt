package com.javimay.uala.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.javimay.uala.ui.theme.Gold
import com.javimay.uala.utils.ZOOM_VALUE

@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    cityName: String,
    latitude: Double,
    longitude: Double,
    isPortrait: Boolean = true,
    navController: NavController,
) {
    val location = LatLng(latitude, longitude)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(location, ZOOM_VALUE)
    }
    val markerState = rememberMarkerState(position = location)

    Box(modifier = modifier) {
        if (isPortrait) {
            Row(
                modifier = Modifier
                    .clickable { navController.popBackStack() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowLeft,
                    contentDescription = "Back",
                    tint = Color.Black
                )
                Text(
                    text = "Back",
                    style = MaterialTheme.typography.titleLarge,
                )
            }
        }
        Spacer(Modifier.padding(vertical = 20.dp))
        GoogleMap(
            modifier = modifier.padding(vertical = 8.dp),
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                state = markerState,
                title = cityName
            )
        }

        LaunchedEffect(location) {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(location, ZOOM_VALUE)
            markerState.position = location
        }
    }
}