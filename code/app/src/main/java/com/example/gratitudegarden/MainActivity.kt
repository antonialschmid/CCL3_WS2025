package com.example.gratitudegarden

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.composable
import com.example.gratitudegarden.screen.GardenScreen
import com.example.gratitudegarden.screen.AddEntryScreen
import com.example.gratitudegarden.ui.theme.GratitudeGardenTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            GratitudeGardenTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = "garden",
            modifier = Modifier.padding(innerPadding)
        ) {

            composable("garden") {
                GardenScreen(navController)
            }

            composable("addEntry") {
                AddEntryScreen(
                    navController = navController,
                    viewModel = TODO("Add ViewModel wiring next")
                )
            }
        }
    }
}
