package com.example.teamoodtracker.ui.history

import com.example.teamoodtracker.data.model.TeaLog
import java.time.LocalDate

/*
 * UI state for history and weekly visualization.
 */
data class HistoryUiState(
  val isLoading: Boolean = true,
  val logs: List<TeaLog> = emptyList(),
  val weeklyCaffeine: LinkedHashMap<LocalDate, Int> = linkedMapOf(),
  val deletingLogId: String? = null
)
