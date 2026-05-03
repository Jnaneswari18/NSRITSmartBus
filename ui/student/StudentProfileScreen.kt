package com.example.smartbus.ui.student

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartbus.data.viewmodel.BusViewModel
import com.example.smartbus.data.models.Student
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun StudentProfileScreen(
    viewModel: BusViewModel = viewModel()
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var isEditing by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    var studentId by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var rollNo by remember { mutableStateOf("") }
    var route by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val profile = viewModel.getStudentProfile()?.await()
        if (profile != null) {
            studentId = profile.id
            fullName = profile.name
            rollNo = profile.rollNumber
            route = profile.route
            phone = profile.phoneNumber
            email = profile.email
        }
        isLoading = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F6FA))   // Soft professional background
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        // ---------- TITLE ----------
        Text(
            text = "Student Profile",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E3C72)
        )

        Spacer(Modifier.height(24.dp))

        // ---------- PROFILE CARD ----------
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

                Text(
                    text = "Personal Information",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1E3C72)
                )

                Spacer(Modifier.height(20.dp))

                ProfileField(
                    label = "Full Name",
                    value = fullName,
                    enabled = isEditing,
                    onValueChange = { fullName = it }
                )

                ProfileField(
                    label = "Roll Number",
                    value = rollNo,
                    enabled = isEditing,
                    onValueChange = { rollNo = it }
                )

                ProfileField(
                    label = "Route",
                    value = route,
                    enabled = isEditing,
                    onValueChange = { route = it }
                )

                ProfileField(
                    label = "Phone Number",
                    value = phone,
                    enabled = isEditing,
                    onValueChange = { phone = it }
                )

                ProfileField(
                    label = "Email Address",
                    value = email,
                    enabled = false, // Email is locked to Firebase Auth
                    onValueChange = { email = it }
                )
            }
        }

        Spacer(Modifier.height(30.dp))

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(androidx.compose.ui.Alignment.CenterHorizontally))
        } else {

        // ---------- EDIT / SAVE BUTTON ----------
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1E3C72)
            ),
            onClick = {
                if (isEditing) {
                    isLoading = true
                    coroutineScope.launch {
                        // Using Java constructor
                        val updated = Student(studentId, fullName, email, rollNo, route, phone)
                        val success = viewModel.updateStudentProfile(updated).await()
                        if (success) {
                            Toast.makeText(context, "Profile Updated Successfully", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Failed to update profile", Toast.LENGTH_SHORT).show()
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
                Spacer(Modifier.width(8.dp))
                Text("Save Changes", color = Color.White)
            } else {
                Icon(Icons.Default.Edit, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Edit Profile", color = Color.White)
            }
        }
        }

        Spacer(Modifier.height(40.dp))
    }
}

//
// ---------- REUSABLE FIELD COMPONENT ----------
//

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

        Spacer(Modifier.height(18.dp))
    }
}



