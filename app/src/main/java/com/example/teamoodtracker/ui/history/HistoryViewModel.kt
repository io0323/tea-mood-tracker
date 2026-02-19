package com.example.teamoodtracker.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teamoodtracker.data.model.Mood
import com.example.teamoodtracker.data.model.TeaLog
import com.example.teamoodtracker.data.model.TimeOfDay
import com.example.teamoodtracker.domain.usecase.DeleteTeaLogUseCase
import com.example.teamoodtracker.domain.usecase.GetAllLogsUseCase
import com.example.teamoodtracker.domain.usecase.GetWeeklyCaffeineUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/*
 * ViewModel for history list and weekly graph.
 */
class HistoryViewModel(
  private val getAllLogsUseCase: GetAllLogsUseCase,
  private val deleteTeaLogUseCase: DeleteTeaLogUseCase,
  private val getWeeklyCaffeineUseCase: GetWeeklyCaffeineUseCase
) : ViewModel() {
  private var allLogsCache: List<TeaLog> = emptyList()
  private val _uiState = MutableStateFlow(HistoryUiState())
  val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

  init {
    observeLogs()
  }

  /*
   * Observe logs and derive weekly chart data.
   */
  private fun observeLogs() {
    viewModelScope.launch {
      getAllLogsUseCase().collect { logs ->
        allLogsCache = logs
        recalculateFilteredState()
      }
    }
  }

  /*
   * Delete one log record and expose progress by id.
   */
  fun deleteLog(logId: String) {
    _uiState.update { it.copy(deletingLogId = logId) }
    deleteTeaLogUseCase(logId)
    _uiState.update { it.copy(deletingLogId = null) }
  }

  /*
   * Update mood filter and rebuild derived state.
   */
  fun setMoodFilter(mood: Mood?) {
    _uiState.update { it.copy(selectedMoodFilter = mood) }
    recalculateFilteredState()
  }

  /*
   * Update time filter and rebuild derived state.
   */
  fun setTimeFilter(timeOfDay: TimeOfDay?) {
    _uiState.update { it.copy(selectedTimeFilter = timeOfDay) }
    recalculateFilteredState()
  }

  /*
   * Reset all active history filters.
   */
  fun clearFilters() {
    _uiState.update {
      it.copy(
        selectedMoodFilter = null,
        selectedTimeFilter = null
      )
    }
    recalculateFilteredState()
  }

  /*
   * Update sort order and rebuild derived state.
   */
  fun setSortOrder(sortOrder: HistorySortOrder) {
    _uiState.update { it.copy(selectedSortOrder = sortOrder) }
    recalculateFilteredState()
  }

  /*
   * Rebuild filtered list and weekly values from cached logs.
   */
  private fun recalculateFilteredState() {
    val current = _uiState.value
    val filteredBase = allLogsCache
      .filter { log ->
        val moodOk = current.selectedMoodFilter?.let { it == log.mood } ?: true
        val timeOk = current.selectedTimeFilter?.let {
          it == log.timeOfDay
        } ?: true
        moodOk && timeOk
      }
    val filtered = when (current.selectedSortOrder) {
      HistorySortOrder.NEWEST -> filteredBase.sortedByDescending { it.date }
      HistorySortOrder.OLDEST -> filteredBase.sortedBy { it.date }
    }

    _uiState.update {
      it.copy(
        isLoading = false,
        logs = filtered,
        weeklyCaffeine = getWeeklyCaffeineUseCase(filtered),
        totalLogCount = allLogsCache.size
      )
    }
  }
}
