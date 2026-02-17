package com.example.teamoodtracker.ui.add

import com.example.teamoodtracker.data.model.Mood
import com.example.teamoodtracker.data.model.TeaType
import com.example.teamoodtracker.data.model.TimeOfDay

/*
 * UI state for Add Log screen.
 */
data class AddLogUiState(
  val selectedMood: Mood = Mood.CALM,
  val selectedTeaType: TeaType = TeaType.GREEN_TEA,
  val selectedTimeOfDay: TimeOfDay = TimeOfDay.MORNING,
  val isSaved: Boolean = false,
  val isSaving: Boolean = false
)
