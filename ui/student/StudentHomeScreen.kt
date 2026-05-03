package com.example.smartbus.ui.student

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import java.util.*
import com.example.smartbus.R

import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartbus.data.viewmodel.BusViewModel

@Composable
fun StudentHomeScreen(
    name: String,
    rollNo: String,
    route: String,
    phone: String,
    viewModel: BusViewModel = viewModel()
) {

    val context = LocalContext.current

    // 🔹 Dynamic content based on time
    val greeting = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
        in 0..11 -> "Good Morning"
        in 12..16 -> "Good Afternoon"
        else -> "Good Evening"
    }

    val userId = viewModel.getCurrentUserId()
    val feeStatus: String by if (userId != null) {
        viewModel.getStudentFeeStatus(userId).observeAsState(initial = "Pending")
    } else {
        remember { mutableStateOf("Pending") }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        if (feeStatus == "Pending") {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFDECEA)),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Text(
                    text = "⚠️ Alert: Your bus fee is pending. Please pay to continue using bus services.",
                    color = Color(0xFFB71C1C),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        // 🔹 Dashboard Title
        Text(
            text = "Student Dashboard",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 🔹 Dynamic Welcome Message
        Text(
            text = "$greeting, $name 👋",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 🔹 NSRIT Information Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {

            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // 🔹 NSRIT Logo
                Image(
                    painter = painterResource(id = R.drawable.nsrit_logo),
                    contentDescription = "NSRIT Logo",
                    modifier = Modifier
                        .size(90.dp)
                        .padding(bottom = 12.dp)
                )

                Text(
                    text = "About NSRIT",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 🔹 About NSRIT – POINTS
                Text("• Autonomous engineering institute")
                Text("• Established in 2008")
                Text("• Offers UG, PG & Diploma programs")
                Text("• Strong placement support")
                Text("• Modern campus with SmartBus Tracking")

                Spacer(modifier = Modifier.height(16.dp))

                Divider()

                Spacer(modifier = Modifier.height(12.dp))

                // 🔹 Address
                Text(
                    text = "📍 Address",
                    style = MaterialTheme.typography.labelLarge
                )
                Text(
                    text = "NSRIT, Sontyam, Pendurthi–Anandapuram Highway,\nVisakhapatnam, Andhra Pradesh – 531173"
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 🔹 Action Buttons Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    // 🌐 Website
                    IconButton(onClick = {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://nsrit.edu.in/")
                        )
                        context.startActivity(intent)
                    }) {
                        Icon(Icons.Default.Public, contentDescription = "Website")
                    }

                    // 📞 Call
                    IconButton(onClick = {
                        val intent = Intent(
                            Intent.ACTION_DIAL,
                            Uri.parse("tel:+918099464546")
                        )
                        context.startActivity(intent)
                    }) {
                        Icon(Icons.Default.Call, contentDescription = "Call")
                    }

                    // 📍 Google Maps
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






