package com.example.smartbus.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.smartbus.data.models.Driver
import com.example.smartbus.data.models.Faculty
import com.example.smartbus.data.models.Student
import com.example.smartbus.data.viewmodel.BusViewModel
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminUserList(
    navController: NavController,
    role: String,
    viewModel: BusViewModel = viewModel()
) {
    var isLoading by remember { mutableStateOf(true) }
    var userList by remember { mutableStateOf<List<Any>>(emptyList()) }

    LaunchedEffect(role) {
        isLoading = true
        userList = when (role) {
            "Student" -> viewModel.getAllStudents().await()
            "Driver" -> viewModel.getAllDrivers().await()
            "Faculty" -> viewModel.getAllFaculty().await()
            else -> emptyList()
        }
        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("$role List") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1E3C72),
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F7FA))
                .padding(paddingValues)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (userList.isEmpty()) {
                Text(
                    text = "No ${role}s found.",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Gray
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(userList) { user ->
                        when (user) {
                            is Student -> StudentListItem(user)
                            is Driver -> DriverListItem(user)
                            is Faculty -> FacultyListItem(user)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StudentListItem(student: Student) {
    UserCard {
        UserDetailRow("Name", student.name)
        UserDetailRow("Roll Number", student.rollNumber)
        UserDetailRow("Email", student.email)
        UserDetailRow("Phone", student.phoneNumber)
        UserDetailRow("Route", student.route)
        UserDetailRow("Fee Status", student.feeStatus, valueColor = if (student.feeStatus == "Paid") Color(0xFF4CAF50) else Color.Red)
    }
}

@Composable
fun DriverListItem(driver: Driver) {
    UserCard {
        UserDetailRow("Name", driver.name)
        UserDetailRow("License Number", driver.licenseNumber)
        UserDetailRow("Bus Number", driver.busNumber)
        UserDetailRow("Email", driver.email)
        UserDetailRow("Phone", driver.phoneNumber)
        UserDetailRow("Route", driver.route)
    }
}

@Composable
fun FacultyListItem(faculty: Faculty) {
    UserCard {
        UserDetailRow("Name", faculty.name)
        UserDetailRow("Faculty ID", faculty.employeeId)
        UserDetailRow("Department", faculty.department)
        UserDetailRow("Email", faculty.email)
        UserDetailRow("Phone", faculty.phoneNumber)
        UserDetailRow("Route", faculty.route)
    }
}

@Composable
fun UserCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            content()
        }
    }
}

@Composable
fun UserDetailRow(label: String, value: String, valueColor: Color = Color.Black) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = valueColor,
            fontWeight = FontWeight.Bold
        )
    }
}
