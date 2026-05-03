package com.example.smartbus.ui.driver

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun DriverRegisterScreen() {

    var name by remember { mutableStateOf("") }
    var licenseNo by remember { mutableStateOf("") }
    var busNumber by remember { mutableStateOf("") }
    var route by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // 🔥 Gradient Background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF0F2027),
                        Color(0xFF203A43),
                        Color(0xFF2C5364)
                    )
                )
            )
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .align(Alignment.Center),
            elevation = CardDefaults.cardElevation(12.dp)
        ) {

            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Driver Registration",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(24.dp))

                CustomDriverField("Driver Name", name) { name = it }
                CustomDriverField("License Number", licenseNo) { licenseNo = it }
                CustomDriverField("Bus Number", busNumber) { busNumber = it }
                CustomDriverField("Route", route) { route = it }
                CustomDriverField("Phone Number", phone) { phone = it }
                CustomDriverField("Password", password) { password = it }

                Spacer(Modifier.height(30.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        // Add navigation or Firebase logic here
                    }
                ) {
                    Text("Register Driver")
                }
            }
        }
    }
}

/* ---------- REUSABLE FIELD ---------- */

@Composable
fun CustomDriverField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        singleLine = true
    )
}

