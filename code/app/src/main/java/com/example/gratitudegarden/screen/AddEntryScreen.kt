package com.example.gratitudegarden.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gratitudegarden.data.model.Mood
import com.example.gratitudegarden.data.viewmodel.AddEntryViewModel
import com.example.gratitudegarden.ui.theme.CardBackground
import com.example.gratitudegarden.ui.theme.TextPrimary
import com.example.gratitudegarden.ui.theme.moodColor
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.material.icons.automirrored.filled.ArrowBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEntryScreen(
    navController: NavController,
    viewModel: AddEntryViewModel
) {
    var text by remember { mutableStateOf("") }
    var selectedMood by remember { mutableStateOf(Mood.PEACEFUL) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Entry") },
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
                .padding(horizontal = 20.dp)
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text("I am thankful for...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(CardBackground, RectangleShape)
                    .border(1.dp, TextPrimary, RectangleShape),
                shape = RectangleShape,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TextPrimary,
                    unfocusedBorderColor = TextPrimary,
                    focusedContainerColor = CardBackground,
                    unfocusedContainerColor = CardBackground
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text("How do you feel today?")

            Spacer(modifier = Modifier.height(12.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Mood.values().forEach { mood ->
                    Box(
                        modifier = Modifier
                            .background(
                                color = if (selectedMood == mood)
                                    moodColor(mood)
                                else
                                    CardBackground,
                                shape = RectangleShape
                            )
                            .border(1.dp, TextPrimary, RectangleShape)
                            .clickable { selectedMood = mood }
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = mood.name.lowercase()
                                .replaceFirstChar { it.uppercase() },
                            color = TextPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    viewModel.saveEntry(
                        text = text,
                        mood = selectedMood.name
                    )
                    navController.popBackStack()
                },
                enabled = text.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                shape = RectangleShape
            ) {
                Text("Save entry")
            }
        }
    }
}