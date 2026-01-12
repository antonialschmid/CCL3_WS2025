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
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.composable
import com.example.gratitudegarden.screen.GardenScreen
import com.example.gratitudegarden.screen.AddEntryScreen
import com.example.gratitudegarden.ui.theme.GratitudeGardenTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.gratitudegarden.data.repository.GratitudeRepository
import com.example.gratitudegarden.data.viewmodel.AddEntryViewModel
import com.example.gratitudegarden.data.viewmodel.AddEntryViewModelFactory
import com.example.gratitudegarden.db.AppDatabase

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

    // Context
    val context = LocalContext.current

    // Database
    val database = AppDatabase.getDatabase(context)

    // DAO
    val dao = database.gratitudeDao()

    // Repository
    val repository = GratitudeRepository(dao)

    // ViewModel Factory
    val factory = AddEntryViewModelFactory(repository)

    // ViewModel (THIS was missing)
    val addEntryViewModel: AddEntryViewModel =
        viewModel(factory = factory)

    val test: AddEntryViewModel = viewModel(factory = factory)

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = "garden",
            modifier = Modifier.padding(innerPadding)
        ) {

            composable("garden") {
                GardenScreen(
                    navController = navController,
                    viewModel = addEntryViewModel
                )
            }

            composable("addEntry") {
                AddEntryScreen(
                    navController = navController,
                    viewModel = addEntryViewModel
                )
            }
        }
    }
}
