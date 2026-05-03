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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.smartbus.data.viewmodel.BusViewModel
import com.example.smartbus.navigation.Routes

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: BusViewModel = viewModel()
) {
    val context = LocalContext.current
    val authState by viewModel.authState.observeAsState(BusViewModel.AuthState.Idle())

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var adminName by remember { mutableStateOf("") }
    var adminPass by remember { mutableStateOf("") }
    var showAdminLogin by remember { mutableStateOf(false) }

    LaunchedEffect(authState) {
        when (authState) {
            is BusViewModel.AuthState.Success -> {
                val role = (authState as BusViewModel.AuthState.Success).role
                Toast.makeText(context, "Login Successful! Role: $role", Toast.LENGTH_SHORT).show()
                viewModel.resetAuthState()

                when (role) {
                    "Student" -> {
                        navController.navigate(Routes.STUDENT_DASHBOARD.replace("{name}", "Student").replace("{rollNo}", "RollNo").replace("{route}", "Route").replace("{phone}", "Phone")) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    }
                    "Driver" -> {
                        navController.navigate(Routes.DRIVER_DASHBOARD.replace("{name}", "Driver").replace("{busNo}", "BusNo").replace("{route}", "Route").replace("{phone}", "Phone")) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    }
                    "Faculty" -> {
                        navController.navigate(Routes.FACULTY_DASHBOARD.replace("{name}", "Faculty").replace("{department}", "Dept").replace("{phone}", "Phone")) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    }
                    "Admin" -> {
                        navController.navigate(Routes.ADMIN_DASHBOARD) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    }
                }
            }
            is BusViewModel.AuthState.Error -> {
                val message = (authState as BusViewModel.AuthState.Error).message
                Toast.makeText(context, "Login Failed: $message", Toast.LENGTH_LONG).show()
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
                        Color(0xFF1E3C72),
                        Color(0xFF2A5298)
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
                    text = "Login",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(20.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(Modifier.height(30.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = authState !is BusViewModel.AuthState.Loading,
                    onClick = {
                        if (email.isBlank() || password.isBlank()) {
                            Toast.makeText(context, "Please enter email and password", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        viewModel.login(email.trim(), password.trim())
                    }
                ) {
                    if (authState is BusViewModel.AuthState.Loading && !showAdminLogin) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Login")
                    }
                }

                Spacer(Modifier.height(16.dp))

                TextButton(onClick = { showAdminLogin = !showAdminLogin }) {
                    Text(if (showAdminLogin) "Back to User Login" else "Admin Access Portal")
                }

                if (showAdminLogin) {
                    Divider(modifier = Modifier.padding(vertical = 16.dp))
                    
                    Text(
                        text = "Internal Administration",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFF1E3C72),
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(16.dp))

                    OutlinedTextField(
                        value = adminName,
                        onValueChange = { adminName = it },
                        label = { Text("Admin Username") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = adminPass,
                        onValueChange = { adminPass = it },
                        label = { Text("Admin Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(Modifier.height(20.dp))

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                        enabled = authState !is BusViewModel.AuthState.Loading,
                        onClick = {
                            if (adminName.isBlank() || adminPass.isBlank()) {
                                Toast.makeText(context, "Please enter admin credentials", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            viewModel.adminLogin(adminName.trim(), adminPass.trim())
                        }
                    ) {
                        if (authState is BusViewModel.AuthState.Loading && showAdminLogin) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text("Confirm Administrative Login", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
