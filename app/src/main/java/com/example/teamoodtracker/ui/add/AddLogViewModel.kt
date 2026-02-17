package com.example.teamoodtracker.ui.add

import androidx.lifecycle.ViewModel
import com.example.teamoodtracker.data.model.Mood
import com.example.teamoodtracker.data.model.TeaLog
import com.example.teamoodtracker.data.model.TeaType
import com.example.teamoodtracker.data.model.TimeOfDay
import com.example.teamoodtracker.domain.usecase.AddTeaLogUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate

/*
 * ViewModel for creating a new tea log.
 */
class AddLogViewModel(
  private val addTeaLogUseCase: AddTeaLogUseCase
) : ViewModel() {
  private val _uiState = MutableStateFlow(AddLogUiState())
  val uiState: StateFlow<AddLogUiState> = _uiState.asStateFlow()

  /*
   * Update selected mood.
   */
  fun onMoodSelected(mood: Mood) {
    _uiState.value = _uiState.value.copy(selectedMood = mood)
  }

  /*
   * Update selected tea type.
   */
  fun onTeaSelected(teaType: TeaType) {
    _uiState.value = _uiState.value.copy(selectedTeaType = teaType)
  }

  /*
   * Update selected time bucket.
   */
  fun onTimeOfDaySelected(timeOfDay: TimeOfDay) {
    _uiState.value = _uiState.value.copy(selectedTimeOfDay = timeOfDay)
  }

  /*
   * Save selected values into a log record.
   */
  fun saveLog() {
    if (_uiState.value.isSaving || _uiState.value.isSaved) {
      return
    }
    _uiState.value = _uiState.value.copy(isSaving = true)
    val current = _uiState.value
    addTeaLogUseCase(
      TeaLog(
        date = LocalDate.now(),
        mood = current.selectedMood,
        teaType = current.selectedTeaType,
        timeOfDay = current.selectedTimeOfDay,
        caffeineAmount = current.selectedTeaType.caffeineMg
      )
    )
    _uiState.value = current.copy(
      isSaving = false,
      isSaved = true
    )
  }

  /*
   * Reset one-shot save flag after handling.
   */
  fun consumeSavedEvent() {
    _uiState.value = _uiState.value.copy(
      isSaved = false,
      isSaving = false
    )
  }
}
