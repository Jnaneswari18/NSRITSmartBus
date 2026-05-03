package com.example.smartbus.ui.student

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun StudentSettingsScreen() {

    val context = LocalContext.current
    val prefs = context.getSharedPreferences("smartbus_settings", Context.MODE_PRIVATE)

    var locationPermission by remember {
        mutableStateOf(prefs.getBoolean("location_permission", false))
    }

    var gpsEnabled by remember {
        mutableStateOf(prefs.getBoolean("gps_enabled", false))
    }

    var notificationsEnabled by remember {
        mutableStateOf(prefs.getBoolean("notifications_enabled", true))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F6FA))   // Soft background
            .padding(20.dp)
    ) {

        // ---------- TITLE ----------
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E3C72)
        )

        Spacer(Modifier.height(24.dp))

        // ---------- SETTINGS CARD ----------
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                SettingsToggleItem(
                    icon = Icons.Default.LocationOn,
                    title = "Location Permission",
                    subtitle = "Allow app to access location",
                    checked = locationPermission
                ) {
                    locationPermission = it
                    prefs.edit().putBoolean("location_permission", it).apply()
                }

                Divider()

                SettingsToggleItem(
                    icon = Icons.Default.GpsFixed,
                    title = "GPS Access",
                    subtitle = "Enable device GPS tracking",
                    checked = gpsEnabled
                ) {
                    gpsEnabled = it
                    prefs.edit().putBoolean("gps_enabled", it).apply()
                }

                Divider()

                SettingsToggleItem(
                    icon = Icons.Default.Notifications,
                    title = "Notifications",
                    subtitle = "Receive bus alerts & updates",
                    checked = notificationsEnabled
                ) {
                    notificationsEnabled = it
                    prefs.edit().putBoolean("notifications_enabled", it).apply()
                }
            }
        }
    }
}

//
// ---------- PROFESSIONAL TOGGLE ITEM ----------
//

@Composable
fun SettingsToggleItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onToggle: (Boolean) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {

            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(28.dp),
                tint = Color(0xFF1E3C72)   // Professional blue
            )

            Spacer(Modifier.width(16.dp))

            Column {

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }

        Switch(
            checked = checked,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF1E3C72)
            )
        )
    }
}



