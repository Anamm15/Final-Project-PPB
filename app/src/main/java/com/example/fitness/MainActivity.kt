package com.example.fitness

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.fitness.ui.navigation.AppNavHost
import com.example.fitness.ui.theme.FitnessTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()  // splash sistem instan (anti layar putih)
        super.onCreate(savedInstanceState)
        setContent {
            FitnessTheme {
                AppNavHost()
            }
        }
    }
}