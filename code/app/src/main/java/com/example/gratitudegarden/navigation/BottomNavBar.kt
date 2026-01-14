package com.example.gratitudegarden.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavBar(
    navController: NavController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {

        // Garden
        NavigationBarItem(
            selected = currentRoute == "garden",
            onClick = {
                navController.navigate("garden") {
                    popUpTo("garden")
                    launchSingleTop = true
                }
            },
            icon = { Icon(Icons.Default.Home, contentDescription = "Garden") },
            label = { Text("Garden") }
        )

        // Add (CENTER)
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("addEntry") },
            icon = {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add entry"
                )
            },
            label = { Text("Add") }
        )

        // History
        NavigationBarItem(
            selected = currentRoute == "history",
            onClick = {
                navController.navigate("history") {
                    popUpTo("garden")
                    launchSingleTop = true
                }
            },
            icon = { Icon(Icons.Default.List, contentDescription = "History") },
            label = { Text("History") }
        )
    }
}