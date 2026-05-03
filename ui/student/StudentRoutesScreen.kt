package com.example.smartbus.ui.student

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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartbus.data.viewmodel.BusViewModel

@Composable
fun StudentRoutesScreen(
    viewModel: BusViewModel = viewModel()
) {
    val context = LocalContext.current
    val liveLocations by viewModel.liveLocations.observeAsState(emptyList())

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
                containerColor = Color(0xFF1E3C72)
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
                    text = "Live Bus Tracking",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )

                Spacer(Modifier.height(8.dp))
                
                if (liveLocations.isEmpty()) {
                    Text("No active buses right now", color = Color.White)
                } else {
                    Text("${liveLocations.size} buses actively tracking", color = Color.White)
                }
            }
        }

        // ---------------- ACTIVE BUS LIST ----------------
        if (liveLocations.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Waiting for buses to start their tracking...", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(liveLocations) { location ->
                    Card(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .fillMaxWidth()
                            .clickable {
                                val lat = location.latitude
                                val lon = location.longitude
                                val route = location.route

                                val gmmIntentUri = Uri.parse("geo:$lat,$lon?q=$lat,$lon(Bus Location: $route)")
                                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                                mapIntent.setPackage("com.google.android.apps.maps")

                                context.startActivity(mapIntent)
                            },
                        elevation = CardDefaults.cardElevation(6.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        ListItem(
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Default.DirectionsBus,
                                    contentDescription = null,
                                    tint = Color(0xFF1E3C72)
                                )
                            },
                            headlineContent = {
                                Text(
                                    text = "Route: ${location.route}",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            },
                            supportingContent = {
                                Text(
                                    text = "Updated: ${java.text.SimpleDateFormat("hh:mm a", java.util.Locale.getDefault()).format(java.util.Date(location.timestamp))}\nTap to view on Map",
                                    color = Color.Gray
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}
