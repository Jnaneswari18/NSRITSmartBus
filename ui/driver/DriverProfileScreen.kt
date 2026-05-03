package com.example.smartbus.ui.driver

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartbus.data.viewmodel.BusViewModel
import com.example.smartbus.data.models.Driver
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun DriverProfileScreen(
    viewModel: BusViewModel = viewModel()
) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var isEditing by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    var driverId by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var busNo by remember { mutableStateOf("") }
    var route by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var licenseNo by remember { mutableStateOf("") }

    // Gradient background (without MaterialTheme.colorScheme)
    val gradientBackground = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFE3F2FD),
            Color.White
        )
    )

    // Fetch driver profile
    LaunchedEffect(Unit) {

        val profile = viewModel.getDriverProfile()?.await()

        if (profile != null) {

            driverId = profile.id
            fullName = profile.name
            email = profile.email
            busNo = profile.busNumber
            route = profile.route
            phone = profile.phoneNumber
            licenseNo = profile.licenseNumber
        }

        isLoading = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        Text(
            text = "Driver Profile",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(10.dp)
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    text = "Driver Information",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(16.dp))

                ProfileField("Full Name", fullName, isEditing) { fullName = it }
                ProfileField("Bus Number", busNo, isEditing) { busNo = it }
                ProfileField("Route", route, isEditing) { route = it }
                ProfileField("Phone Number", phone, isEditing) { phone = it }
                ProfileField("License Number", licenseNo, isEditing) { licenseNo = it }
                ProfileField("Email Address", email, false) { email = it }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        if (isLoading) {

            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

        } else {

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),

                onClick = {

                    if (isEditing) {

                        isLoading = true

                        coroutineScope.launch {

                            val updatedDriver = Driver(
                                driverId,
                                fullName,
                                email,
                                busNo,
                                route,
                                phone,
                                licenseNo
                            )

                            val success = viewModel.updateDriverProfile(updatedDriver).await()

                            if (success) {
                                Toast.makeText(
                                    context,
                                    "Profile Updated Successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Failed to update profile",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            isEditing = false
                            isLoading = false
                        }

                    } else {
                        isEditing = true
                    }

                }
            ) {

                if (isEditing) {

                    Icon(Icons.Default.Save, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Save Changes")

                } else {

                    Icon(Icons.Default.Edit, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Edit Profile")
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun ProfileField(
    label: String,
    value: String,
    enabled: Boolean,
    onValueChange: (String) -> Unit
) {

    Column {

        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = Color.Gray
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}
