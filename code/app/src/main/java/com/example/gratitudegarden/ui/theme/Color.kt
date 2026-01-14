package com.example.gratitudegarden.ui.theme

import androidx.compose.ui.graphics.Color
import com.example.gratitudegarden.data.model.Mood
// App base colors
val AppBackground = Color(0xFFEEEFE9)
val NavBarBackground = Color(0xFFF4F5F1)
val CardBackground = Color(0xFFFFFFFF)

// Text
val TextPrimary = Color(0xFF5A5752)
val TextSecondary = Color(0xFF8A8782)

// Mood colors
val MoodHappy = Color(0xFFE3BCC6)
val MoodPeaceful = Color(0xFFB9CDB2)
val MoodGrateful = Color(0xFFDDAB9B)
val MoodHopeful = Color(0xFF95BAD2)
val MoodCalm = Color(0xFFF5EFAE)

// Mood → Color mapping
fun moodColor(mood: Mood): Color {
    return when (mood) {
        Mood.HAPPY -> MoodHappy
        Mood.PEACEFUL -> MoodPeaceful
        Mood.GRATEFUL -> MoodGrateful
        Mood.HOPEFUL -> MoodHopeful
        Mood.CALM -> MoodCalm
    }
}