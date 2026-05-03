package com.example.smartbus.ui.driver

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DriverDashboard(
    name: String,
    busNo: String,
    route: String,
    phone: String
) {

    var selectedIndex by rememberSaveable { mutableStateOf(0) }

    val items = listOf("Home", "GPS", "Profile")

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,

        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        label = { Text(item) },
                        icon = {
                            when (item) {
                                "Home" -> Icon(Icons.Default.Home, null)
                                "GPS" -> Icon(Icons.Default.LocationOn, null)
                                "Profile" -> Icon(Icons.Default.Person, null)
                            }
                        }
                    )
                }
            }
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            when (selectedIndex) {
                0 -> DriverHomeScreen(name, busNo, route)
                1 -> DriverGPSScreen(route = route)
                2 -> DriverProfileScreen()
            }
        }
    }
}


