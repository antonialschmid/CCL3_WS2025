package com.example.gratitudegarden.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.gratitudegarden.R
import com.example.gratitudegarden.data.model.Mood
import com.example.gratitudegarden.data.viewmodel.AddEntryViewModel
import com.example.gratitudegarden.ui.theme.AppBackground
import com.example.gratitudegarden.ui.theme.CardBackground
import com.example.gratitudegarden.ui.theme.MoodCalm
import com.example.gratitudegarden.ui.theme.MoodGrateful
import com.example.gratitudegarden.ui.theme.MoodPeaceful
import com.example.gratitudegarden.ui.theme.TextPrimary
import com.example.gratitudegarden.ui.theme.moodColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailEntryScreen(
    navController: NavController,
    viewModel: AddEntryViewModel,
    entryId: Long
) {
    val entry = viewModel.getEntryById(entryId)

    if (entry == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Entry not found", color = TextPrimary)
        }
        return
    }

    var isEditing by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf(entry.text) }
    var selectedMood by remember {
        mutableStateOf(Mood.valueOf(entry.mood))
    }

    val isAnyMoodSelected = true

    Scaffold(
        containerColor = AppBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (isEditing) "Edit Entry" else "Entry Details",
                        color = TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppBackground
                )
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
                enabled = isEditing,
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
                    unfocusedContainerColor = CardBackground,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    disabledContainerColor = CardBackground,
                    disabledTextColor = TextPrimary
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "How do you feel today?",
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Mood.values().forEach { mood ->

                    val backgroundColor =
                        if (selectedMood == mood) {
                            moodColor(mood)
                        } else {
                            moodColor(mood).copy(alpha = 0.2f)
                        }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .background(backgroundColor, RectangleShape)
                            .border(1.dp, TextPrimary, RectangleShape)
                            .clickable(enabled = isEditing) {
                                selectedMood = mood
                            }
                            .padding(vertical = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(id = moodIcon(mood)),
                            contentDescription = mood.name,
                            tint = TextPrimary,
                            modifier = Modifier.size(16.dp)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = mood.name.lowercase()
                                .replaceFirstChar { it.uppercase() },
                            color = TextPrimary,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            if (!isEditing) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, TextPrimary, RectangleShape)
                        .background(MoodCalm, RectangleShape)
                        .clickable { isEditing = true }
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Edit entry", color = TextPrimary)
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, TextPrimary, RectangleShape)
                        .background(MoodPeaceful, RectangleShape)
                        .clickable {
                            viewModel.updateEntry(
                                entry.copy(
                                    text = text,
                                    mood = selectedMood.name
                                )
                            )
                            navController.popBackStack()
                        }
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Save changes", color = TextPrimary)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, TextPrimary, RectangleShape)
                    .background(MoodGrateful, RectangleShape)
                    .clickable {
                        viewModel.deleteEntry(entry)
                        navController.popBackStack()
                    }
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Delete", color = TextPrimary)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

private fun moodIcon(mood: Mood): Int {
    return when (mood) {
        Mood.HAPPY -> R.drawable.ic_mood_happy
        Mood.PEACEFUL -> R.drawable.ic_mood_peaceful
        Mood.GRATEFUL -> R.drawable.ic_mood_grateful
        Mood.HOPEFUL -> R.drawable.ic_mood_hopeful
        Mood.CALM -> R.drawable.ic_mood_content
    }
}