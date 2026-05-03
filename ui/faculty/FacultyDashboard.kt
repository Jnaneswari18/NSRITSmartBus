package com.example.smartbus.ui.faculty

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FacultyDashboard(
    name: String,
    department: String,
    phone: String
) {

    var selectedIndex by rememberSaveable { mutableStateOf(0) }

    val items = listOf("Home", "Routes", "Profile", "Settings")
    val icons = listOf(
        Icons.Default.Home,
        Icons.Default.Map,
        Icons.Default.Person,
        Icons.Default.Settings
    )

    Scaffold(
        containerColor = Color(0xFFF4F6FA),   // Soft background
        bottomBar = {

            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {

                items.forEachIndexed { index, item ->

                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        icon = {
                            Icon(
                                imageVector = icons[index],
                                contentDescription = item
                            )
                        },
                        label = { Text(item) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF1E3C72),
                            selectedTextColor = Color(0xFF1E3C72),
                            indicatorColor = Color(0xFFE3ECFA)
                        )
                    )
                }
            }
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF4F6FA))
                .padding(paddingValues)
        ) {

            when (selectedIndex) {

                0 -> FacultyHomeScreen(name, department)

                1 -> FacultyRoutesScreen()

                2 -> FacultyProfileScreen(name, department, phone)

                3 -> FacultySettingsScreen()
            }
        }
    }
}

