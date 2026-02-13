package com.example.teamoodtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.teamoodtracker.core.AppContainer
import com.example.teamoodtracker.ui.TeaMoodApp
import com.example.teamoodtracker.ui.theme.TeaMoodTrackerTheme

/*
 * Entry activity that hosts the compose app.
 */
class MainActivity : ComponentActivity() {
  /*
   * Inflate and start the compose hierarchy.
   */
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val appContainer = AppContainer()
    setContent {
      TeaMoodTrackerTheme {
        TeaMoodApp(appContainer = appContainer)
      }
    }
  }
}
