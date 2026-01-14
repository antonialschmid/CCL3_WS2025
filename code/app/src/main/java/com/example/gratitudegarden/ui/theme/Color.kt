package com.example.gratitudegarden.ui.theme

import androidx.compose.ui.graphics.Color
import com.example.gratitudegarden.data.model.Mood

// 🌍 App Backgrounds
val AppBackground = Color(0xFFEEEFE9)
val NavBarBackground = Color(0xFFF4F5F1)
val CardBackground = Color(0xFFFFFFFF)

// 🌿 Accent / Active States
val NavBarActive = Color(0xFFB9CDB2)

// ✏️ Text
val TextPrimary = Color(0xFF5A5752)
val TextSecondary = Color(0xFF9CA3AF)

// ➖ Borders & Dividers (optional, ruhig)
val BorderLight = Color(0xFFD1D5DB)



val MoodHappy = Color(0xFFE3BCC6)
val MoodPeaceful = Color(0xFFB9CDB2)
val MoodGrateful = Color(0xFFDDAB9B)
val MoodHopeful = Color(0xFF95BAD2)
val MoodCalm = Color(0xFFF5EFAE)

fun moodColor(mood: Mood): Color {
    return when (mood) {
        Mood.HAPPY -> MoodHappy
        Mood.PEACEFUL -> MoodPeaceful
        Mood.GRATEFUL -> MoodGrateful
        Mood.HOPEFUL -> MoodHopeful
        Mood.CALM -> MoodCalm
    }
}