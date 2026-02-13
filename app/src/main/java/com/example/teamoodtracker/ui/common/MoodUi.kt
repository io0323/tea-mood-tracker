package com.example.teamoodtracker.ui.common

import androidx.compose.ui.graphics.Color
import com.example.teamoodtracker.data.model.Mood

/*
 * Map mood to accent color for UI effects.
 */
fun Mood.toAccentColor(): Color {
  return when (this) {
    Mood.CALM -> Color(0xFF66BB6A)
    Mood.HAPPY -> Color(0xFFFFB300)
    Mood.TIRED -> Color(0xFF5C6BC0)
    Mood.STRESSED -> Color(0xFFEF5350)
  }
}
