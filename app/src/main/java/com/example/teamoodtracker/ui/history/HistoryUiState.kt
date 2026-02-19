package com.example.teamoodtracker.ui.history

import com.example.teamoodtracker.data.model.Mood
import com.example.teamoodtracker.data.model.TeaLog
import com.example.teamoodtracker.data.model.TimeOfDay
import java.time.LocalDate

/*
 * Sort order options for history list.
 */
enum class HistorySortOrder(val label: String) {
  NEWEST("Newest"),
  OLDEST("Oldest")
}

/*
 * UI state for history and weekly visualization.
 */
data class HistoryUiState(
  val isLoading: Boolean = true,
  val logs: List<TeaLog> = emptyList(),
  val weeklyCaffeine: LinkedHashMap<LocalDate, Int> = linkedMapOf(),
  val deletingLogId: String? = null,
  val selectedMoodFilter: Mood? = null,
  val selectedTimeFilter: TimeOfDay? = null,
  val totalLogCount: Int = 0,
  val selectedSortOrder: HistorySortOrder = HistorySortOrder.NEWEST
)
