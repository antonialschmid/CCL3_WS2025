package com.example.gratitudegarden.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val GratitudeColorScheme = lightColorScheme(
    primary = NavBarActive,
    onPrimary = TextPrimary,

    secondary = NavBarBackground,
    onSecondary = TextPrimary,

    background = AppBackground,
    onBackground = TextPrimary,

    surface = CardBackground,
    onSurface = TextPrimary
)

@Composable
fun GratitudeGardenTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = GratitudeColorScheme,
        typography = Typography,
        content = content
    )
}