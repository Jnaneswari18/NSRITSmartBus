package com.example.smartbus.ui.faculty

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.smartbus.ui.common.SharedLocationManager

@Composable
fun FacultyRoutesScreen() {

    val context = LocalContext.current

    val driverLat by SharedLocationManager.getLatitude().observeAsState("0.000000")
    val driverLon by SharedLocationManager.getLongitude().observeAsState("0.000000")

    val buses = ('A'..'Z').map { "$it Bus" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F6FA))
    ) {

        // ---------------- HEADER CARD ----------------

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF0D47A1)   // Slightly darker blue for faculty
            )
        ) {

            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    text = "Live Transport Monitoring",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Latitude : $driverLat",
                    color = Color.White
                )

                Text(
                    text = "Longitude : $driverLon",
                    color = Color.White
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = "Real-time Bus Supervision",
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }

        // ---------------- BUS LIST ----------------

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {

            items(buses) { bus ->

                Card(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .fillMaxWidth()
                        .clickable {

                            val lat = driverLat
                            val lon = driverLon

                            val gmmIntentUri = Uri.parse(
                                "geo:$lat,$lon?q=$lat,$lon($bus Location)"
                            )

                            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                            mapIntent.setPackage("com.google.android.apps.maps")

                            context.startActivity(mapIntent)
                        },
                    elevation = CardDefaults.cardElevation(6.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {

                    ListItem(
                        leadingContent = {
                            Icon(
                                imageVector = Icons.Default.DirectionsBus,
                                contentDescription = null,
                                tint = Color(0xFF0D47A1)
                            )
                        },
                        headlineContent = {
                            Text(
                                text = bus,
                                style = MaterialTheme.typography.titleMedium
                            )
                        },
                        supportingContent = {
                            Text(
                                text = "Tap to monitor live tracking",
                                color = Color.Gray
                            )
                        }
                    )
                }
            }
        }
    }
}

