package com.example.teamoodtracker.ui.add

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teamoodtracker.data.model.Mood
import com.example.teamoodtracker.data.model.TeaType
import com.example.teamoodtracker.data.model.TimeOfDay
import com.example.teamoodtracker.ui.common.toAccentColor
import kotlinx.coroutines.delay

/*
 * Route composable for the Add Log screen.
 */
@Composable
fun AddLogRoute(
  viewModel: AddLogViewModel,
  onSaved: () -> Unit
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  LaunchedEffect(uiState.isSaved) {
    if (uiState.isSaved) {
      delay(700)
      onSaved()
      viewModel.consumeSavedEvent()
    }
  }

  AddLogScreen(
    uiState = uiState,
    onMoodSelected = viewModel::onMoodSelected,
    onTeaSelected = viewModel::onTeaSelected,
    onTimeOfDaySelected = viewModel::onTimeOfDaySelected,
    onSaveClicked = viewModel::saveLog,
    onResetClicked = viewModel::resetSelections
  )
}

/*
 * Main Add Log screen UI.
 */
@Composable
fun AddLogScreen(
  uiState: AddLogUiState,
  onMoodSelected: (Mood) -> Unit,
  onTeaSelected: (TeaType) -> Unit,
  onTimeOfDaySelected: (TimeOfDay) -> Unit,
  onSaveClicked: () -> Unit,
  onResetClicked: () -> Unit
) {
  val canReset = uiState.selectedMood != Mood.CALM
    || uiState.selectedTeaType != TeaType.GREEN_TEA
    || uiState.selectedTimeOfDay != TimeOfDay.MORNING

  val bgColor by animateColorAsState(
    targetValue = uiState.selectedMood.toAccentColor().copy(alpha = 0.12f),
    label = "mood_bg"
  )

  LazyColumn(
    modifier = Modifier
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      Text(
        text = "Add Log",
        style = MaterialTheme.typography.headlineMedium
      )
    }
    item {
      AnimatedVisibility(
        visible = uiState.isSaved,
        enter = fadeIn(),
        exit = fadeOut()
      ) {
        Card(
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
          )
        ) {
          Text(
            modifier = Modifier.padding(14.dp),
            text = "Saved successfully. Returning...",
            style = MaterialTheme.typography.bodyMedium
          )
        }
      }
    }
    item {
      SelectionSection(
        title = "Mood",
        background = bgColor,
        options = Mood.entries,
        selected = uiState.selectedMood,
        labelOf = { mood -> "${mood.emoji} ${mood.label}" },
        onSelected = onMoodSelected
      )
    }
    item {
      SelectionSection(
        title = "Tea",
        background = bgColor,
        options = TeaType.entries,
        selected = uiState.selectedTeaType,
        labelOf = { teaType -> "${teaType.label} (${teaType.caffeineMg}mg)" },
        onSelected = onTeaSelected
      )
    }
    item {
      SelectionSection(
        title = "Time of day",
        background = bgColor,
        options = TimeOfDay.entries,
        selected = uiState.selectedTimeOfDay,
        labelOf = { timeOfDay -> timeOfDay.label },
        onSelected = onTimeOfDaySelected
      )
    }
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        OutlinedButton(
          onClick = onResetClicked,
          enabled = canReset && !uiState.isSaving,
          modifier = Modifier.weight(1f)
        ) {
          Text(text = "Reset")
        }
        Button(
          onClick = onSaveClicked,
          enabled = !uiState.isSaving && !uiState.isSaved,
          modifier = Modifier.weight(1f)
        ) {
          Text(
            text = when {
              uiState.isSaving -> "Saving..."
              uiState.isSaved -> "Saved"
              else -> "Save"
            }
          )
        }
      }
    }
  }
}

/*
 * Generic selectable chip section.
 */
@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun <T> SelectionSection(
  title: String,
  background: androidx.compose.ui.graphics.Color,
  options: List<T>,
  selected: T,
  labelOf: (T) -> String,
  onSelected: (T) -> Unit
) {
  androidx.compose.material3.Card(
    colors = androidx.compose.material3.CardDefaults.cardColors(
      containerColor = background
    )
  ) {
    Column(
      modifier = Modifier.padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Text(text = title, style = MaterialTheme.typography.titleMedium)
      FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        options.forEach { option ->
          FilterChip(
            selected = option == selected,
            onClick = { onSelected(option) },
            label = { Text(labelOf(option)) }
          )
        }
      }
    }
  }
}
