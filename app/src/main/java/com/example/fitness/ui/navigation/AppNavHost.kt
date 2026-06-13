package com.example.fitness.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fitness.ui.screen.ActivityScreen
import com.example.fitness.ui.screen.AdminDashboardScreen
import com.example.fitness.ui.screen.DashboardScreen
import com.example.fitness.ui.screen.HomeScreen
import com.example.fitness.ui.screen.LoginScreen
import com.example.fitness.ui.screen.PaymentScreen
import com.example.fitness.ui.screen.RegistrasiScreen
import com.example.fitness.ui.screen.RewardScreen
import com.example.fitness.ui.screen.SplashScreen

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onTimeout = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Home.route) {
            HomeScreen(
                onRegisterClick = { navController.navigate(Screen.Register.route) },
                onLoginClick = { navController.navigate(Screen.Login.route) }
            )
        }
        composable(Screen.Register.route) {
            RegistrasiScreen(
                onRegistered = { userId, tierId ->
                    navController.navigate(Screen.Payment.createRoute(userId, tierId))
                },
                onLoginClick = { navController.navigate(Screen.Login.route) }
            )
        }
        composable(Screen.Login.route) {
            LoginScreen(
                onLoggedIn = { userId, role ->
                    val destination =
                        if (role == "ADMIN") Screen.AdminDashboard.route
                        else Screen.Dashboard.createRoute(userId)
                    navController.navigate(destination) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onRegisterClick = { navController.navigate(Screen.Register.route) }
            )
        }
        composable(
            route = Screen.Payment.route,
            arguments = listOf(
                navArgument("userId") { type = NavType.LongType },
                navArgument("tierId") { type = NavType.LongType }
            )
        ) {
            PaymentScreen(
                onPaid = { userId ->
                    navController.navigate(Screen.Dashboard.createRoute(userId)) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
        composable(
            route = Screen.Dashboard.route,
            arguments = listOf(navArgument("userId") { type = NavType.LongType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getLong("userId") ?: 0L
            DashboardScreen(
                onStartActivity = { navController.navigate(Screen.Activity.createRoute(userId)) },
                onOpenRewards = { navController.navigate(Screen.Reward.createRoute(userId)) }
            )
        }
        composable(Screen.AdminDashboard.route) {
            AdminDashboardScreen(
                onLogout = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        composable(
            route = Screen.Activity.route,
            arguments = listOf(navArgument("userId") { type = NavType.LongType })
        ) {
            ActivityScreen(
                onDone = { navController.popBackStack() }
            )
        }
        composable(
            route = Screen.Reward.route,
            arguments = listOf(navArgument("userId") { type = NavType.LongType })
        ) {
            RewardScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}