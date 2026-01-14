package com.example.gratitudegarden.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gratitudegarden.data.viewmodel.AddEntryViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailEntryScreen(
    navController: NavController,
    viewModel: AddEntryViewModel,
    entryId: Long
) {
    val entry = viewModel.getEntryById(entryId)

    var isEditing by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf(entry?.text ?: "") }
    var selectedMood by remember { mutableStateOf(entry?.mood ?: "Peaceful") }

    if (entry == null) {
        Text("Entry not found")
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (isEditing) "Edit Entry" else "Entry Details")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
        ) {

            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                enabled = isEditing,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("How do you feel today?")

            Row {
                listOf("Happy", "Peaceful", "Grateful", "Hopeful").forEach { mood ->
                    Button(
                        onClick = { selectedMood = mood },
                        enabled = isEditing,
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Text(mood)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (!isEditing) {
                Button(
                    onClick = { isEditing = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Edit entry")
                }
            } else {
                Button(
                    onClick = {
                        viewModel.updateEntry(
                            entry.copy(
                                text = text,
                                mood = selectedMood
                            )
                        )
                        navController.popBackStack()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save changes")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = {
                    viewModel.deleteEntry(entry)
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Delete")
            }
        }
    }
}
