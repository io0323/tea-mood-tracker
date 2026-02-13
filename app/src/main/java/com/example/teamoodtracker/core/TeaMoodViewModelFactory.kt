package com.example.teamoodtracker.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.teamoodtracker.ui.add.AddLogViewModel
import com.example.teamoodtracker.ui.history.HistoryViewModel
import com.example.teamoodtracker.ui.home.HomeViewModel

/*
 * Factory that provides ViewModels with use cases.
 */
class TeaMoodViewModelFactory(
  private val container: AppContainer
) : ViewModelProvider.Factory {
  /*
   * Create ViewModel instances from the app container.
   */
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    return when {
      modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
        HomeViewModel(
          getAllLogsUseCase = container.getAllLogsUseCase,
          getTodayLogsUseCase = container.getTodayLogsUseCase
        ) as T
      }

      modelClass.isAssignableFrom(AddLogViewModel::class.java) -> {
        AddLogViewModel(
          addTeaLogUseCase = container.addTeaLogUseCase
        ) as T
      }

      modelClass.isAssignableFrom(HistoryViewModel::class.java) -> {
        HistoryViewModel(
          getAllLogsUseCase = container.getAllLogsUseCase,
          getWeeklyCaffeineUseCase = container.getWeeklyCaffeineUseCase
        ) as T
      }

      else -> error("Unknown ViewModel class: ${modelClass.simpleName}")
    }
  }
}
