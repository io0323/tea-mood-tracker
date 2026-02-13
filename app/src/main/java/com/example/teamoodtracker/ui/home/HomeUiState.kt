package com.example.teamoodtracker.ui.home

import com.example.teamoodtracker.data.model.Mood
import com.example.teamoodtracker.data.model.TeaLog
import com.example.teamoodtracker.data.model.TeaType

/*
 * UI state for the Today screen.
 */
data class HomeUiState(
  val isLoading: Boolean = true,
  val todayLogs: List<TeaLog> = emptyList(),
  val dominantMood: Mood? = null,
  val teasToday: List<TeaType> = emptyList(),
  val caffeineTodayMg: Int = 0
)
