package com.example.smartbus.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.smartbus.ui.auth.*
import com.example.smartbus.ui.student.*
import com.example.smartbus.ui.driver.*
import com.example.smartbus.ui.faculty.*

@Composable
fun AppNavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.START
    ) {

        // ---------------- START ----------------
        composable(Routes.START) {
            StartScreen(navController)
        }

        // ---------------- LOGIN ----------------
        composable(Routes.LOGIN) {
            LoginScreen(navController)
        }

        // ---------------- REGISTER ----------------
        composable(Routes.REGISTER) {
            RegisterScreen(navController)
        }

        // ---------------- STUDENT ----------------
        composable(
            route = Routes.STUDENT_DASHBOARD,
            arguments = listOf(
                navArgument("name") { type = NavType.StringType },
                navArgument("rollNo") { type = NavType.StringType },
                navArgument("route") { type = NavType.StringType },
                navArgument("phone") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            StudentDashboard(
                name = backStackEntry.arguments?.getString("name") ?: "",
                rollNo = backStackEntry.arguments?.getString("rollNo") ?: "",
                route = backStackEntry.arguments?.getString("route") ?: "",
                phone = backStackEntry.arguments?.getString("phone") ?: ""
            )
        }

        // ---------------- DRIVER ----------------
        composable(
            route = Routes.DRIVER_DASHBOARD,
            arguments = listOf(
                navArgument("name") { type = NavType.StringType },
                navArgument("busNo") { type = NavType.StringType },
                navArgument("route") { type = NavType.StringType },
                navArgument("phone") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            DriverDashboard(
                name = backStackEntry.arguments?.getString("name") ?: "",
                busNo = backStackEntry.arguments?.getString("busNo") ?: "",
                route = backStackEntry.arguments?.getString("route") ?: "",
                phone = backStackEntry.arguments?.getString("phone") ?: ""
            )
        }

        // ---------------- FACULTY ----------------
        composable(
            route = Routes.FACULTY_DASHBOARD,
            arguments = listOf(
                navArgument("name") { type = NavType.StringType },
                navArgument("department") { type = NavType.StringType },
                navArgument("phone") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            FacultyDashboard(
                name = backStackEntry.arguments?.getString("name") ?: "",
                department = backStackEntry.arguments?.getString("department") ?: "",
                phone = backStackEntry.arguments?.getString("phone") ?: ""
            )
        }

        // ---------------- ADMIN ----------------
        composable(Routes.ADMIN_DASHBOARD) {
            com.example.smartbus.ui.admin.AdminDashboard(navController)
        }

        composable(
            route = Routes.ADMIN_USER_LIST,
            arguments = listOf(navArgument("role") { type = NavType.StringType })
        ) { backStackEntry ->
            val role = backStackEntry.arguments?.getString("role") ?: "Student"
            com.example.smartbus.ui.admin.AdminUserList(navController, role)
        }
    }
}
