package com.example.gratitudegarden.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.example.gratitudegarden.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GardenScreen(
    navController: NavController
) {
    Scaffold(
        containerColor = AppBackground,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppBackground
                ),
                title = {
                    Column {
                        Text(
                            text = "My Garden",
                            color = TextPrimary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "3 moments of gratitude",
                            color = TextSecondary,
                            fontSize = 13.sp
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { navController.navigate("settings") }
                    ) {
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

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .background(
                        color = CardBackground,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = TextPrimary,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("<", color = TextPrimary, fontSize = 18.sp)

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "12.02.2026",
                    color = TextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(">", color = TextPrimary, fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(36.dp))

            Box(
                modifier = Modifier
                    .size(320.dp)
                    .padding(top = 120.dp),
                contentAlignment = Alignment.Center
            ) {
                MonthlyDotsCircle(
                    daysInMonth = 31,
                    modifier = Modifier.fillMaxSize()
                )

                PlantStageImage(
                    totalEntries = 3
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
    daysInMonth: Int,
    modifier: Modifier = Modifier
) {
    val today = java.time.LocalDate.now().dayOfMonth

    Canvas(modifier = modifier) {
        val radius = size.minDimension / 1.2f
        val center = this.center

        for (day in 1..daysInMonth) {
            val angleDegrees = (360f / daysInMonth) * (day - 1) - 90f
            val angleRad = Math.toRadians(angleDegrees.toDouble())

            val x = center.x + radius * cos(angleRad).toFloat()
            val y = center.y + radius * sin(angleRad).toFloat()
            val dotCenter = Offset(x, y)

            drawCircle(
                color = Color.LightGray,
                radius = 20f,
                center = dotCenter
            )

            if (day == today) {
                drawCircle(
                    color = TextPrimary,
                    radius = 25f,
                    center = dotCenter,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(
                        width = 5f
                    )
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