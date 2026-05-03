package com.example.smartbus.ui.driver

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.smartbus.R
import java.util.*

@Composable
fun DriverHomeScreen(
    name: String,
    busNo: String,
    route: String
) {

    val context = LocalContext.current

    val greeting = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
        in 0..11 -> "Good Morning"
        in 12..16 -> "Good Afternoon"
        else -> "Good Evening"
    }

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
            MaterialTheme.colorScheme.background
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground)
            .padding(20.dp)
    ) {

        // ---------- HEADER ----------
        Text(
            text = "Driver Dashboard",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = "$greeting, $name 👋",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(Modifier.height(24.dp))

        // ---------- DRIVER INFO CARD ----------
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(10.dp)
        ) {

            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Text(
                    text = "Driver Details",
                    style = MaterialTheme.typography.titleMedium
                )

                Divider()

                Text("🚌 Bus Number : $busNo")
                Text("📍 Route : $route")
                Text("📡 Tracking Status : Active")
            }
        }

        Spacer(Modifier.height(24.dp))

        // ---------- NSRIT CARD ----------
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(10.dp)
        ) {

            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    painter = painterResource(id = R.drawable.nsrit_logo),
                    contentDescription = "NSRIT Logo",
                    modifier = Modifier.size(80.dp)
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "About NSRIT",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(Modifier.height(12.dp))

                Text("• Autonomous engineering institute")
                Text("• Established in 2008")
                Text("• UG, PG & Diploma Programs")
                Text("• Strong placement support")
                Text("• Smart Bus Tracking Enabled")

                Spacer(Modifier.height(16.dp))

                Divider()

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "📍 Address",
                    style = MaterialTheme.typography.labelLarge
                )

                Text(
                    text = "NSRIT, Sontyam, Pendurthi–Anandapuram Highway,\nVisakhapatnam, Andhra Pradesh – 531173"
                )

                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    IconButton(onClick = {
                        context.startActivity(
                            Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://nsrit.edu.in/"))
                        )
                    }) {
                        Icon(Icons.Default.Public, contentDescription = "Website")
                    }

                    IconButton(onClick = {
                        context.startActivity(
                            Intent(Intent.ACTION_DIAL,
                                Uri.parse("tel:+918099464546"))
                        )
                    }) {
                        Icon(Icons.Default.Call, contentDescription = "Call")
                    }

                    IconButton(onClick = {
                        val gmmIntentUri = Uri.parse(
                            "geo:0,0?q=NSRIT+Engineering+College+Visakhapatnam"
                        )
                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                        mapIntent.setPackage("com.google.android.apps.maps")
                        context.startActivity(mapIntent)
                    }) {
                        Icon(Icons.Default.LocationOn, contentDescription = "Location")
                    }
                }
            }
        }
    }
}

