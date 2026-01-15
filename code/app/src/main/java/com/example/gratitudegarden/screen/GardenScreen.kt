package com.example.gratitudegarden.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.gratitudegarden.data.model.GratitudeEntry
import com.example.gratitudegarden.data.model.Mood
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
                    .background(CardBackground, RoundedCornerShape(6.dp))
                    .border(1.dp, TextPrimary, RoundedCornerShape(6.dp))
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.ChevronLeft,
                    contentDescription = "Previous day",
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { selectedDate = selectedDate.minusDays(1) },
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
                        .clickable { selectedDate = selectedDate.plusDays(1) },
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
                    month = selectedDate,
                    entries = entries,
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
}

@Composable
fun MonthlyDotsCircle(
    month: LocalDate,
    entries: List<GratitudeEntry>,
    modifier: Modifier = Modifier
) {
    val daysInMonth = month.lengthOfMonth()
    val today = LocalDate.now()

    Canvas(modifier = modifier) {
        val radius = size.minDimension / 1.1f
        val center = this.center

        drawCircle(
            color = TextPrimary.copy(alpha = 0.08f),
            radius = radius,
            center = center,
            style = androidx.compose.ui.graphics.drawscope.Stroke(6f)
        )

        for (day in 1..daysInMonth) {
            val date = month.withDayOfMonth(day)

            val angleDegrees = (360f / daysInMonth) * (day - 1) - 90f
            val angleRad = Math.toRadians(angleDegrees.toDouble())

            val x = center.x + radius * cos(angleRad).toFloat()
            val y = center.y + radius * sin(angleRad).toFloat()
            val dotCenter = Offset(x, y)

            val entryForDay = entries
                .filter {
                    Instant.ofEpochMilli(it.timestamp)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate() == date
                }
                .minByOrNull { it.timestamp }

            val dotColor =
                entryForDay?.let {
                    moodColor(Mood.valueOf(it.mood))
                } ?: Color.LightGray

            // main dot
            drawCircle(
                color = dotColor,
                radius = 22f,
                center = dotCenter
            )

            // white ring for days with entry
            if (entryForDay != null) {
                drawCircle(
                    color = Color.White,
                    radius = 26f,
                    center = dotCenter,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(3f)
                )
            }

            // today ring (bigger + spaced)
            if (date == today) {
                drawCircle(
                    color = TextPrimary.copy(alpha = 0.6f),
                    radius = 32f,
                    center = dotCenter,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(4f)
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