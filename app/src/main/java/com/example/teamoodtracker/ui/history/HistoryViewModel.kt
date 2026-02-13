package com.example.teamoodtracker.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teamoodtracker.domain.usecase.GetAllLogsUseCase
import com.example.teamoodtracker.domain.usecase.GetWeeklyCaffeineUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/*
 * ViewModel for history list and weekly graph.
 */
class HistoryViewModel(
  private val getAllLogsUseCase: GetAllLogsUseCase,
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
        _uiState.value = HistoryUiState(
          isLoading = false,
          logs = logs.sortedByDescending { it.date },
          weeklyCaffeine = getWeeklyCaffeineUseCase(logs)
        )
      }
    }
  }
}
