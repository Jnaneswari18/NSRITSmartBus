package com.example.smartbus.ui.faculty

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.smartbus.R
import java.util.*

@Composable
fun FacultyHomeScreen(
    name: String,
    department: String
) {

    val context = LocalContext.current

    // Dynamic greeting
    val greeting = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
        in 0..11 -> "Good Morning"
        in 12..16 -> "Good Afternoon"
        else -> "Good Evening"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Faculty Dashboard",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "$greeting, Prof. $name 👋",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {

            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    painter = painterResource(id = R.drawable.nsrit_logo),
                    contentDescription = "NSRIT Logo",
                    modifier = Modifier
                        .size(90.dp)
                        .padding(bottom = 12.dp)
                )

                Text(
                    text = "Faculty Information",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Department: $department")
                Text("Academic Role: Supervisor")
                Text("Transport Monitoring: Enabled")
                Text("SmartBus Supervision: Active")

                Spacer(modifier = Modifier.height(16.dp))

                HorizontalDivider()

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "📍 Campus Address",
                    style = MaterialTheme.typography.labelLarge
                )

                Text(
                    text = "NSRIT, Sontyam, Pendurthi–Anandapuram Highway,\nVisakhapatnam, Andhra Pradesh – 531173"
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    // Website
                    IconButton(onClick = {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            android.net.Uri.parse("https://nsrit.edu.in/")
                        )
                        context.startActivity(intent)
                    }) {
                        Icon(Icons.Default.Public, contentDescription = "Website")
                    }

                    // Call
                    IconButton(onClick = {
                        val intent = Intent(
                            Intent.ACTION_DIAL,
                            android.net.Uri.parse("tel:+918099464546")
                        )
                        context.startActivity(intent)
                    }) {
                        Icon(Icons.Default.Call, contentDescription = "Call")
                    }

                    // Maps
                    IconButton(onClick = {
                        val gmmIntentUri = android.net.Uri.parse(
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
