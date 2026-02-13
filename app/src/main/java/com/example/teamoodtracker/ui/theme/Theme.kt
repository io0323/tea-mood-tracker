package com.example.teamoodtracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColors = darkColorScheme(
  primary = Green80,
  secondary = Blue80,
  background = Night
)

private val LightColors = lightColorScheme(
  primary = Green40,
  secondary = Blue40,
  background = Cream
)

/*
 * App-level Material3 theme setup.
 */
@Composable
fun TeaMoodTrackerTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit
) {
  val colors = if (darkTheme) DarkColors else LightColors
  MaterialTheme(
    colorScheme = colors,
    typography = Typography,
    content = content
  )
}
