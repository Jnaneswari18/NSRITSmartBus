package com.example.smartbus.ui.auth

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.smartbus.data.models.Driver
import com.example.smartbus.data.models.Faculty
import com.example.smartbus.data.models.Student
import com.example.smartbus.data.viewmodel.BusViewModel
import com.example.smartbus.navigation.Routes

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: BusViewModel = viewModel()
) {
    val context = LocalContext.current
    val authState by viewModel.authState.observeAsState(BusViewModel.AuthState.Idle())

    var selectedRole by remember { mutableStateOf("Student") }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var route by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    
    // Student specific
    var rollNo by remember { mutableStateOf("") }
    
    // Driver specific
    var busNumber by remember { mutableStateOf("") }
    var licenseNo by remember { mutableStateOf("") }
    
    // Faculty specific
    var facultyId by remember { mutableStateOf("") }
    var department by remember { mutableStateOf("") }

    LaunchedEffect(authState) {
        when (authState) {
            is BusViewModel.AuthState.Success -> {
                val role = (authState as BusViewModel.AuthState.Success).role
                Toast.makeText(context, "$role Registration Successful!", Toast.LENGTH_SHORT).show()
                viewModel.resetAuthState()
                
                when (role) {
                    "Student" -> {
                        val encName = java.net.URLEncoder.encode(name.ifEmpty { "NA" }, "UTF-8")
                        val encRoll = java.net.URLEncoder.encode(rollNo.ifEmpty { "NA" }, "UTF-8")
                        val encRoute = java.net.URLEncoder.encode(route.ifEmpty { "NA" }, "UTF-8")
                        val encPhone = java.net.URLEncoder.encode(phone.ifEmpty { "NA" }, "UTF-8")
                        navController.navigate(Routes.STUDENT_DASHBOARD.replace("{name}", encName).replace("{rollNo}", encRoll).replace("{route}", encRoute).replace("{phone}", encPhone)) {
                            popUpTo(Routes.REGISTER) { inclusive = true }
                        }
                    }
                    "Driver" -> {
                        val encName = java.net.URLEncoder.encode(name.ifEmpty { "NA" }, "UTF-8")
                        val encBus = java.net.URLEncoder.encode(busNumber.ifEmpty { "NA" }, "UTF-8")
                        val encRoute = java.net.URLEncoder.encode(route.ifEmpty { "NA" }, "UTF-8")
                        val encPhone = java.net.URLEncoder.encode(phone.ifEmpty { "NA" }, "UTF-8")
                        navController.navigate(Routes.DRIVER_DASHBOARD.replace("{name}", encName).replace("{busNo}", encBus).replace("{route}", encRoute).replace("{phone}", encPhone)) {
                            popUpTo(Routes.REGISTER) { inclusive = true }
                        }
                    }
                    "Faculty" -> {
                        val encName = java.net.URLEncoder.encode(name.ifEmpty { "NA" }, "UTF-8")
                        val encDept = java.net.URLEncoder.encode(department.ifEmpty { "NA" }, "UTF-8")
                        val encPhone = java.net.URLEncoder.encode(phone.ifEmpty { "NA" }, "UTF-8")
                        navController.navigate(Routes.FACULTY_DASHBOARD.replace("{name}", encName).replace("{department}", encDept).replace("{phone}", encPhone)) {
                            popUpTo(Routes.REGISTER) { inclusive = true }
                        }
                    }
                }
            }
            is BusViewModel.AuthState.Error -> {
                val message = (authState as BusViewModel.AuthState.Error).message
                Toast.makeText(context, "Error: $message", Toast.LENGTH_LONG).show()
                viewModel.resetAuthState()
            }
            else -> {}
        }
    }

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
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Create Account",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RoleOption("Student", selectedRole) { selectedRole = it }
                    RoleOption("Driver", selectedRole) { selectedRole = it }
                    RoleOption("Faculty", selectedRole) { selectedRole = it }
                }

                Spacer(modifier = Modifier.height(20.dp))

                CustomField("Full Name", name) { name = it }
                CustomField("Email", email) { email = it }
                CustomField("Password", password, isPassword = true) { password = it }

                if (selectedRole == "Student") {
                    CustomField("Roll Number", rollNo) { rollNo = it }
                    CustomField("Route", route) { route = it }
                    CustomField("Phone", phone) { phone = it }
                }

                if (selectedRole == "Driver") {
                    CustomField("License Number", licenseNo) { licenseNo = it }
                    CustomField("Bus Number", busNumber) { busNumber = it }
                    CustomField("Route", route) { route = it }
                    CustomField("Phone", phone) { phone = it }
                }

                if (selectedRole == "Faculty") {
                    CustomField("Faculty ID", facultyId) { facultyId = it }
                    CustomField("Department", department) { department = it }
                    CustomField("Phone", phone) { phone = it }
                }

                Spacer(modifier = Modifier.height(30.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = authState !is BusViewModel.AuthState.Loading,
                    onClick = {
                        if (email.isBlank() || password.isBlank() || name.isBlank()) {
                            Toast.makeText(context, "Name, Email and Password are required", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        when (selectedRole) {
                            "Student" -> {
                                if (rollNo.isBlank() || route.isBlank() || phone.isBlank()) {
                                    Toast.makeText(context, "Please fill in all student details", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                val student = Student("", name.trim(), email.trim(), rollNo.trim(), route.trim(), phone.trim())
                                viewModel.registerStudent(email.trim(), password.trim(), student)
                            }
                            "Driver" -> {
                                if (licenseNo.isBlank() || busNumber.isBlank() || route.isBlank() || phone.isBlank()) {
                                    Toast.makeText(context, "Please fill in all driver details", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                val driver = Driver("", name.trim(), email.trim(), busNumber.trim(), route.trim(), phone.trim(), licenseNo.trim())
                                viewModel.registerDriver(email.trim(), password.trim(), driver)
                            }
                            "Faculty" -> {
                                if (facultyId.isBlank() || department.isBlank() || phone.isBlank()) {
                                    Toast.makeText(context, "Please fill in all faculty details", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                val faculty = Faculty("", name.trim(), email.trim(), department.trim(), route.trim(), phone.trim(), facultyId.trim())
                                viewModel.registerFaculty(email.trim(), password.trim(), faculty)
                            }
                        }
                    }
                ) {
                    if (authState is BusViewModel.AuthState.Loading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Register")
                    }
                }
            }
        }
    }
}

/* ---------------- ROLE OPTION ---------------- */

@Composable
fun RoleOption(
    role: String,
    selectedRole: String,
    onSelect: (String) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
            selected = selectedRole == role,
            onClick = { onSelect(role) }
        )
        Text(text = role)
    }
}

/* ---------------- CUSTOM TEXT FIELD ---------------- */

@Composable
fun CustomField(
    label: String,
    value: String,
    isPassword: Boolean = false,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        visualTransformation = if (isPassword) androidx.compose.ui.text.input.PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
            keyboardType = if (isPassword) androidx.compose.ui.text.input.KeyboardType.Password else androidx.compose.ui.text.input.KeyboardType.Text,
            imeAction = androidx.compose.ui.text.input.ImeAction.Next
        ),
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    )
}