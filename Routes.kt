package com.example.smartbus.navigation

object Routes {

    // -------- BASIC ROUTES --------
    const val START = "start"
    const val LOGIN = "login"
    const val REGISTER = "register"

    // -------- STUDENT --------
    const val STUDENT_DASHBOARD = "student_dashboard/{name}/{rollNo}/{route}/{phone}"

    fun studentDashboardRoute(name: String, rollNo: String, route: String, phone: String): String {
        return "student_dashboard/$name/$rollNo/$route/$phone"
    }

    // -------- DRIVER --------
    const val DRIVER_DASHBOARD = "driver_dashboard/{name}/{busNo}/{route}/{phone}"

    fun driverDashboardRoute(name: String, busNo: String, route: String, phone: String): String {
        return "driver_dashboard/$name/$busNo/$route/$phone"
    }

    // -------- FACULTY --------
    const val FACULTY_DASHBOARD = "faculty_dashboard/{name}/{department}/{phone}"

    fun facultyDashboardRoute(name: String, department: String, phone: String): String {
        return "faculty_dashboard/$name/$department/$phone"
    }

    // -------- ADMIN --------
    const val ADMIN_DASHBOARD = "admin_dashboard"
    const val ADMIN_USER_LIST = "admin_user_list/{role}"

    fun adminUserListRoute(role: String): String {
        return "admin_user_list/$role"
    }
}
