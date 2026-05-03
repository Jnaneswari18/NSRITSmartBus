package com.example.smartbus.ui.driver

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartbus.data.viewmodel.BusViewModel
import com.google.android.gms.location.*

@Composable
fun DriverGPSScreen(
    route: String,
    viewModel: BusViewModel = viewModel()
) {
    val context = LocalContext.current

    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    var latitude by remember { mutableStateOf("Waiting...") }
    var longitude by remember { mutableStateOf("Waiting...") }
    var isTracking by remember { mutableStateOf(false) }

    val locationRequest = remember {
        LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            5000
        ).build()
    }

    var locationCallback by remember { mutableStateOf<LocationCallback?>(null) }

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
            MaterialTheme.colorScheme.background
        )
    )

    val permissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (granted) {
                startTracking(
                    fusedLocationClient,
                    locationRequest
                ) { lat, lon ->
                    latitude = lat
                    longitude = lon
                    viewModel.updateDriverLocation(lat.toDoubleOrNull() ?: 0.0, lon.toDoubleOrNull() ?: 0.0, route)
                }.also {
                    locationCallback = it
                }

                isTracking = true
            }
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            modifier = Modifier.size(70.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = "Live GPS Tracking",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(10.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Latitude: $latitude")
                Text("Longitude: $longitude")
                Text("Route: $route")

                Spacer(Modifier.height(12.dp))

                Text(
                    text = if (isTracking) "Tracking Active 🟢" else "Tracking Stopped 🔴",
                    color = if (isTracking) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
            }
        }

        Spacer(Modifier.height(30.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            onClick = {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    startTracking(
                        fusedLocationClient,
                        locationRequest
                    ) { lat, lon ->
                        latitude = lat
                        longitude = lon
                        viewModel.updateDriverLocation(lat.toDoubleOrNull() ?: 0.0, lon.toDoubleOrNull() ?: 0.0, route)
                    }.also {
                        locationCallback = it
                    }
                    isTracking = true
                } else {
                    permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }
        ) {
            Icon(Icons.Default.PlayArrow, null)
            Spacer(Modifier.width(8.dp))
            Text("Start Sharing")
        }

        Spacer(Modifier.height(12.dp))

        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            onClick = {
                locationCallback?.let {
                    fusedLocationClient.removeLocationUpdates(it)
                }
                isTracking = false
            }
        ) {
            Icon(Icons.Default.Stop, null)
            Spacer(Modifier.width(8.dp))
            Text("Stop Sharing")
        }
    }
}

@SuppressLint("MissingPermission")
fun startTracking(
    fusedLocationClient: FusedLocationProviderClient,
    locationRequest: LocationRequest,
    onLocationUpdate: (String, String) -> Unit
): LocationCallback {

    val callback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            result.lastLocation?.let {
                onLocationUpdate(
                    it.latitude.toString(),
                    it.longitude.toString()
                )
            }
        }
    }
    fusedLocationClient.requestLocationUpdates(
        locationRequest,
        callback,
        null
    )
    return callback
}
