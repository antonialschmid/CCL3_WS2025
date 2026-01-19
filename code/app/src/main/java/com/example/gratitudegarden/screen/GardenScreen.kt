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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gratitudegarden.R
import com.example.gratitudegarden.data.model.GratitudeEntry
import com.example.gratitudegarden.data.model.Mood
import com.example.gratitudegarden.data.viewmodel.AddEntryViewModel
import com.example.gratitudegarden.ui.theme.*
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
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

    var selectedMonth by remember {
        mutableStateOf(java.time.YearMonth.now())
    }

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
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                actions = {
                    IconButton(onClick = { navController.navigate("settings") }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_settings),
                            contentDescription = "Settings",
                            tint = TextPrimary,
                            modifier = Modifier.size(28.dp)
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
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CardBackground)
                    .border(1.dp, TextPrimary)
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.ChevronLeft,
                    contentDescription = "Previous month",
                    modifier = Modifier
                        .size(32.dp)
                        .clickable {
                            selectedMonth = selectedMonth.minusMonths(1)
                        },
                    tint = TextPrimary
                )

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = selectedMonth.month.name
                            .lowercase()
                            .replaceFirstChar { it.uppercase() } +
                                " " + selectedMonth.year,
                        color = TextPrimary,
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Next month",
                    modifier = Modifier
                        .size(32.dp)
                        .clickable {
                            selectedMonth = selectedMonth.plusMonths(1)
                        },
                    tint = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(400.dp)
                    .padding(top = 100.dp)
            ) {

                Box(
                    modifier = Modifier.size(180.dp),
                    contentAlignment = Alignment.Center
                ) {
                    MonthlyDotsCircle(
                        month = selectedMonth,
                        entries = entries,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                PlantStageImage(totalEntries = entryCount)
            }

            Spacer(modifier = Modifier.height(100.dp))

            Text(
                text = "Your plant is starting to grow. Keep nurturing it.",
                color = TextSecondary,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }
    }
}

@Composable
fun MonthlyDotsCircle(
    month: java.time.YearMonth,
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
            val date = month.atDay(day)

            val angle = (360f / daysInMonth) * (day - 1) - 90f
            val rad = Math.toRadians(angle.toDouble())

            val dotCenter = Offset(
                x = center.x + radius * cos(rad).toFloat(),
                y = center.y + radius * sin(rad).toFloat()
            )

            val entryForDay = entries.firstOrNull {
                Instant.ofEpochMilli(it.timestamp)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate() == date
            }

            val isToday = date == today
            val dotRadius = if (isToday) 42f else 26f
            val dotColor =
                entryForDay?.let { moodColor(Mood.valueOf(it.mood)) }
                    ?: Color.LightGray

            drawCircle(
                color = dotColor,
                radius = dotRadius,
                center = dotCenter
            )

            if (entryForDay != null) {
                drawCircle(
                    color = TextPrimary,
                    radius = dotRadius,
                    center = dotCenter,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(3f)
                )
            }

            if (isToday) {
                drawCircle(
                    color = TextPrimary.copy(alpha = 0.6f),
                    radius = dotRadius + 6f,
                    center = dotCenter,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(4f)
                )

                drawContext.canvas.nativeCanvas.apply {
                    val paint = android.graphics.Paint().apply {
                        color = android.graphics.Color.BLACK
                        textAlign = android.graphics.Paint.Align.CENTER
                        textSize = 38f
                        isAntiAlias = true
                        typeface = android.graphics.Typeface.DEFAULT_BOLD
                    }

                    val yOffset =
                        (paint.descent() + paint.ascent()) / 2

                    drawText(
                        day.toString(),
                        dotCenter.x,
                        dotCenter.y - yOffset,
                        paint
                    )
                }
            }
        }
    }
}

@Composable
fun PlantStageImage(totalEntries: Int) {
    val stageRes = when {
        totalEntries < 0 -> R.drawable.plant_stage_1
        totalEntries < 1 -> R.drawable.plant_stage_2
        totalEntries < 2 -> R.drawable.plant_stage_3
        totalEntries < 3 -> R.drawable.plant_stage_4
        else -> R.drawable.plant_stage_5
    }

    Image(
        painter = painterResource(stageRes),
        contentDescription = "Plant growth stage",
        modifier = Modifier.size(280.dp)
    )
}