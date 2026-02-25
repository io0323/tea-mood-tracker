package com.example.teamoodtracker.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teamoodtracker.data.model.TeaType
import com.example.teamoodtracker.data.model.TimeOfDay
import com.example.teamoodtracker.domain.usecase.GetAllLogsUseCase
import com.example.teamoodtracker.domain.usecase.GetTodayLogsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

/*
 * ViewModel for the Today screen.
 */
class HomeViewModel(
  private val getAllLogsUseCase: GetAllLogsUseCase,
  private val getTodayLogsUseCase: GetTodayLogsUseCase
) : ViewModel() {
  private val _uiState = MutableStateFlow(HomeUiState())
  val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

  init {
    observeLogs()
  }

  /*
   * Observe global logs and derive today metrics.
   */
  private fun observeLogs() {
    viewModelScope.launch {
      getAllLogsUseCase().collect { allLogs ->
        val todayLogs = getTodayLogsUseCase()
        val dominantMood = todayLogs.lastOrNull()?.mood
        val teasToday = todayLogs.map { log -> log.teaType }
        val teaTypeCount = TeaType.entries.associateWith { type ->
          todayLogs.count { log -> log.teaType == type }
        }
        val caffeine = todayLogs.sumOf { log -> log.caffeineAmount }
        val logsCount = todayLogs.size
        val timeCounts = TimeOfDay.entries.associateWith { bucket ->
          todayLogs.count { log -> log.timeOfDay == bucket }
        }
        val today = LocalDate.now()
        val weekStart = today.minusDays(6)
        val weeklyTotal = allLogs
          .filter { log -> log.date in weekStart..today }
          .sumOf { log -> log.caffeineAmount }
        val weeklyAverage = weeklyTotal / 7

        _uiState.value = HomeUiState(
          isLoading = false,
          todayLogs = todayLogs,
          dominantMood = dominantMood,
          teasToday = teasToday,
          teaTypeCountToday = teaTypeCount,
          caffeineTodayMg = caffeine,
          logsCountToday = logsCount,
          timeOfDayCount = timeCounts,
          weeklyCaffeineTotalMg = weeklyTotal,
          weeklyCaffeineAverageMg = weeklyAverage
        )
      }
    }
  }
}
