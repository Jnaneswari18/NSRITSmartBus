package com.example.smartbus.ui.student

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun StudentDashboard(
    name: String,
    rollNo: String,
    route: String,
    phone: String
) {


    var selectedIndex by remember { mutableIntStateOf(0) }

    val items = listOf(
        "Home",
        "Routes",
        "Fees",
        "Profile",
        "Settings"
    )

    Scaffold(

        // 🔹 TOP BAR
        topBar = {
            TopAppBar(
                title = {
                    Text("Student Dashboard")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1E3C72),
                    titleContentColor = Color.White
                )
            )
        },

        // 🔹 BOTTOM NAVIGATION
        bottomBar = {
            NavigationBar(
                containerColor = Color.White
            ) {

                items.forEachIndexed { index, item ->

                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        label = { Text(item) },
                        icon = {
                            when (item) {
                                "Home" -> Icon(Icons.Default.Home, null)
                                "Routes" -> Icon(Icons.Default.DirectionsBus, null)
                                "Fees" -> Icon(Icons.Default.AccountBalanceWallet, null)
                                "Profile" -> Icon(Icons.Default.Person, null)
                                "Settings" -> Icon(Icons.Default.Settings, null)
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF1E3C72),
                            selectedTextColor = Color(0xFF1E3C72),
                            indicatorColor = Color(0xFFE3F2FD)
                        )
                    )
                }
            }
        }

    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F7FA))
        ) {

            when (selectedIndex) {

                0 -> StudentHomeScreen(
                    name = name,
                    rollNo = rollNo,
                    route = route,
                    phone = phone
                )

                1 -> StudentRoutesScreen()

                2 -> StudentFeesScreen()

                3 -> StudentProfileScreen()

                4 -> StudentSettingsScreen()
            }
        }
    }
}
