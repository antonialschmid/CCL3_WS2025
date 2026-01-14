package com.example.gratitudegarden.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gratitudegarden.ui.theme.*

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
                Text(
                    text = "<",
                    color = TextPrimary,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "12.02.2026",
                    color = TextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = ">",
                    color = TextPrimary,
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

            Box(
                modifier = Modifier
                    .size(220.dp)
                    .background(
                        color = CardBackground,
                        shape = CircleShape
                    )
                    .border(
                        width = 1.dp,
                        color = TextPrimary,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Plant",
                    color = TextPrimary,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Your plant is starting to grow.\nKeep nurturing it.",
                color = TextSecondary,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
    }
}