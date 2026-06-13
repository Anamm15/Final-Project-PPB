package com.example.fitness.ui.navigation

// Screen.kt
sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Home : Screen("home")
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object Dashboard : Screen("dashboard/{userId}") {
        fun createRoute(userId: Long) = "dashboard/$userId"
    }

    data object AdminDashboard : Screen("admin_dashboard")

    data object Activity : Screen("activity/{userId}") {
        fun createRoute(userId: Long) = "activity/$userId"
    }
    data object Reward : Screen("reward/{userId}") {
        fun createRoute(userId: Long) = "reward/$userId"
    }

    data object Payment : Screen("payment/{userId}/{tierId}") {
        fun createRoute(userId: Long, tierId: Long) = "payment/$userId/$tierId"
    }
}