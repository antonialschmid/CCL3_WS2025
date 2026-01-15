package com.example.gratitudegarden.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gratitudegarden.R
import com.example.gratitudegarden.data.viewmodel.AddEntryViewModel
import com.example.gratitudegarden.ui.theme.*
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GardenScreen(
    navController: NavController,
    viewModel: AddEntryViewModel
) {
    val entries by viewModel.entries.collectAsState()
    val entryCount = entries.size

    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var showDatePicker by remember { mutableStateOf(false) }

    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")

    Scaffold(
        containerColor = AppBackground,
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppBackground
                ),
                title = {
                    Text(
                        text = "Garden",
                        color = TextPrimary,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Medium
                    )
                },
                actions = {
                    IconButton(onClick = { navController.navigate("settings") }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = TextPrimary
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
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = when (entryCount) {
                    0 -> "No moments of gratitude yet"
                    1 -> "1 moment of gratitude"
                    else -> "$entryCount moments of gratitude"
                },
                color = TextSecondary,
                fontSize = 13.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, TextPrimary, )
                    .background(CardBackground, )
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.ChevronLeft,
                    contentDescription = "Previous day",
                    modifier = Modifier
                        .size(32.dp)
                        .clickable {
                            selectedDate = selectedDate.minusDays(1)
                        },
                    tint = TextPrimary
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { showDatePicker = true },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = selectedDate.format(formatter),
                        color = TextPrimary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Next day",
                    modifier = Modifier
                        .size(32.dp)
                        .clickable {
                            selectedDate = selectedDate.plusDays(1)
                        },
                    tint = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

            Box(
                modifier = Modifier
                    .size(320.dp)
                    .padding(top = 120.dp),
                contentAlignment = Alignment.Center
            ) {
                MonthlyDotsCircle(
                    daysInMonth = selectedDate.lengthOfMonth(),
                    modifier = Modifier.fillMaxSize()
                )

                PlantStageImage(
                    totalEntries = entryCount
                )
            }

            Spacer(modifier = Modifier.height(90.dp))

            Text(
                text = "Your plant is starting to grow. Keep nurturing it.",
                color = TextSecondary,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        selectedDate = Instant.ofEpochMilli(it)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
fun MonthlyDotsCircle(
    daysInMonth: Int,
    modifier: Modifier = Modifier
) {
    val today = LocalDate.now().dayOfMonth

    Canvas(modifier = modifier) {
        val radius = size.minDimension / 1.2f
        val center = this.center

        for (day in 1..daysInMonth) {
            val angleDegrees = (360f / daysInMonth) * (day - 1) - 90f
            val angleRad = Math.toRadians(angleDegrees.toDouble())

            val x = center.x + radius * cos(angleRad).toFloat()
            val y = center.y + radius * sin(angleRad).toFloat()

            drawCircle(
                color = Color.LightGray,
                radius = 20f,
                center = Offset(x, y)
            )

            if (day == today) {
                drawCircle(
                    color = TextPrimary,
                    radius = 25f,
                    center = Offset(x, y),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(5f)
                )
            }
        }
    }
}

@Composable
fun PlantStageImage(
    totalEntries: Int
) {
    val stageRes = when {
        totalEntries < 3 -> R.drawable.plant_stage_1
        totalEntries < 6 -> R.drawable.plant_stage_2
        totalEntries < 10 -> R.drawable.plant_stage_3
        totalEntries < 15 -> R.drawable.plant_stage_4
        else -> R.drawable.plant_stage_5
    }

    Image(
        painter = painterResource(id = stageRes),
        contentDescription = "Plant growth stage",
        modifier = Modifier.size(80.dp)
    )
}