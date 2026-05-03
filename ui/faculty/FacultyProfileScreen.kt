package com.example.smartbus.ui.faculty

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.smartbus.data.models.Faculty
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun FacultyProfileScreen(
    name: String,
    department: String,
    phone: String,
    viewModel: BusViewModel = viewModel()
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var isEditing by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    var facultyId by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf(name) }
    var dept by remember { mutableStateOf(department) }
    var phoneNumber by remember { mutableStateOf(phone) }
    var email by remember { mutableStateOf("") }
    var route by remember { mutableStateOf("") }
    var employeeId by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val profile = viewModel.getFacultyProfile()?.await()
        if (profile != null) {
            facultyId = profile.id
            fullName = profile.name
            email = profile.email
            dept = profile.department
            route = profile.route
            phoneNumber = profile.phoneNumber
            employeeId = profile.employeeId
        }
        isLoading = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F6FA))   // Premium light background
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        // ---------------- HEADER ----------------

        Text(
            text = "Faculty Profile",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E3C72)
        )

        Spacer(Modifier.height(30.dp))

        // ---------------- PROFILE CARD ----------------

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                SectionTitle("Account Information")

                Spacer(Modifier.height(16.dp))

                ProfileField(
                    label = "Full Name",
                    value = fullName,
                    enabled = isEditing,
                    onValueChange = { fullName = it }
                )

                ProfileField(
                    label = "Department",
                    value = dept,
                    enabled = isEditing,
                    onValueChange = { dept = it }
                )

                ProfileField(
                    label = "Phone Number",
                    value = phoneNumber,
                    enabled = isEditing,
                    onValueChange = { phoneNumber = it }
                )

                ProfileField(
                    label = "Route",
                    value = route,
                    enabled = isEditing,
                    onValueChange = { route = it }
                )

                ProfileField(
                    label = "Employee ID",
                    value = employeeId,
                    enabled = isEditing,
                    onValueChange = { employeeId = it }
                )

                ProfileField(
                    label = "Email Address",
                    value = email,
                    enabled = false, // Locked to Auth
                    onValueChange = { email = it }
                )

                Spacer(Modifier.height(24.dp))

                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(androidx.compose.ui.Alignment.CenterHorizontally))
                } else {

                Button(
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1E3C72)
                    ),
                    onClick = {
                        if (isEditing) {
                            isLoading = true
                            coroutineScope.launch {
                                // Using Java constructor
                                val updated = Faculty(facultyId, fullName, email, dept, route, phoneNumber, employeeId)
                                val success = viewModel.updateFacultyProfile(updated).await()
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
            }
        }

        Spacer(Modifier.height(40.dp))
    }
}

//
// ---------------- SECTION TITLE ----------------
//

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xFF1E3C72)
    )
}

//
// ---------------- REUSABLE FIELD ----------------
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

        Spacer(Modifier.height(4.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(16.dp))
    }
}
