package com.example.teamoodtracker.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
        _uiState.update { current ->
          current.copy(
            isLoading = false,
            logs = logs.sortedByDescending { it.date },
            weeklyCaffeine = getWeeklyCaffeineUseCase(logs)
          )
        }
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
}
