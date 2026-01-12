package com.example.gratitudegarden

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.composable
import com.example.gratitudegarden.screen.GardenScreen
import com.example.gratitudegarden.screen.AddEntryScreen
import com.example.gratitudegarden.ui.theme.GratitudeGardenTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.gratitudegarden.data.repository.GratitudeRepository
import com.example.gratitudegarden.data.viewmodel.AddEntryViewModel
import com.example.gratitudegarden.data.viewmodel.AddEntryViewModelFactory
import com.example.gratitudegarden.db.AppDatabase
import com.example.gratitudegarden.navigation.BottomNavBar
import androidx.compose.material3.Icon
import com.example.gratitudegarden.screen.DetailEntryScreen
import com.example.gratitudegarden.screen.HistoryScreen
import com.example.gratitudegarden.screen.SettingsScreen

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
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context)
    val dao = database.gratitudeDao()
    val repository = GratitudeRepository(dao)
    val factory = AddEntryViewModelFactory(repository)
    val addEntryViewModel: AddEntryViewModel =
        viewModel(factory = factory)

    val test: AddEntryViewModel = viewModel(factory = factory)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar = currentRoute in listOf(
        "garden",
        "history",
        "settings"
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(navController)
            }
        },
        floatingActionButton = {
            if (showBottomBar) {
                FloatingActionButton(
                    onClick = { navController.navigate("addEntry") }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add entry"
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
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

            composable("history") {
                HistoryScreen(
                    navController = navController,
                    viewModel = addEntryViewModel
                )
            }
            composable(
                route = "detail/{entryId}"
            ) { backStackEntry ->
                val entryId = backStackEntry.arguments
                    ?.getString("entryId")
                    ?.toInt() ?: return@composable

                DetailEntryScreen(
                    navController = navController,
                    viewModel = addEntryViewModel,
                    entryId = entryId
                )
            }
            composable("settings") {
                SettingsScreen(navController = navController)
            }
        }
    }
}
