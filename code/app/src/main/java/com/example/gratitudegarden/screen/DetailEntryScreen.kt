package com.example.gratitudegarden.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gratitudegarden.data.model.GratitudeEntry
import com.example.gratitudegarden.data.viewmodel.AddEntryViewModel

@Composable
fun DetailEntryScreen(
    navController: NavController,
    viewModel: AddEntryViewModel,
    entryId: Int
) {
    var entry by remember { mutableStateOf<GratitudeEntry?>(null) }
    var text by remember { mutableStateOf("") }

    LaunchedEffect(entryId) {
        entry = viewModel.getEntryById(entryId)
        text = entry?.text.orEmpty()
    }

    if (entry == null) {
        Text("Loading…")
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Text(
            text = "Edit Entry",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.updateEntry(entry!!.copy(text = text))
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save changes")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = {
                viewModel.deleteEntry(entry!!)
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Delete entry")
        }
    }
}
