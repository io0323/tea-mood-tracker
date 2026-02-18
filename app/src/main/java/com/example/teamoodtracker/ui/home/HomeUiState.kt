package com.example.teamoodtracker.ui.home

import com.example.teamoodtracker.data.model.Mood
import com.example.teamoodtracker.data.model.TeaLog
import com.example.teamoodtracker.data.model.TeaType
import com.example.teamoodtracker.data.model.TimeOfDay

/*
 * UI state for the Today screen.
 */
data class HomeUiState(
  val isLoading: Boolean = true,
  val todayLogs: List<TeaLog> = emptyList(),
  val dominantMood: Mood? = null,
  val teasToday: List<TeaType> = emptyList(),
  val caffeineTodayMg: Int = 0,
  val logsCountToday: Int = 0,
  val timeOfDayCount: Map<TimeOfDay, Int> = emptyMap()
)
