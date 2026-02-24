package com.example.teamoodtracker.ui.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teamoodtracker.data.model.Mood
import com.example.teamoodtracker.data.model.TeaLog
import com.example.teamoodtracker.data.model.TimeOfDay
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/*
 * Route composable for the History screen.
 */
@Composable
fun HistoryRoute(viewModel: HistoryViewModel) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  HistoryScreen(
    uiState = uiState,
    onDeleteLog = viewModel::deleteLog,
    onTodayOnlyChanged = viewModel::setTodayOnly,
    onMoodFilterChanged = viewModel::setMoodFilter,
    onTimeFilterChanged = viewModel::setTimeFilter,
    onSortOrderChanged = viewModel::setSortOrder,
    onClearFilters = viewModel::clearFilters
  )
}

/*
 * Main history UI including graph and list.
 */
@Composable
fun HistoryScreen(
  uiState: HistoryUiState,
  onDeleteLog: (String) -> Unit,
  onTodayOnlyChanged: (Boolean) -> Unit,
  onMoodFilterChanged: (Mood?) -> Unit,
  onTimeFilterChanged: (TimeOfDay?) -> Unit,
  onSortOrderChanged: (HistorySortOrder) -> Unit,
  onClearFilters: () -> Unit
) {
  var pendingDeleteLogId by remember { mutableStateOf<String?>(null) }

  if (pendingDeleteLogId != null) {
    DeleteConfirmDialog(
      onConfirm = {
        pendingDeleteLogId?.let { onDeleteLog(it) }
        pendingDeleteLogId = null
      },
      onDismiss = { pendingDeleteLogId = null }
    )
  }

  if (!uiState.isLoading && uiState.logs.isEmpty() && uiState.totalLogCount == 0) {
    EmptyState(
      title = "No logs yet.",
      body = "Add your first tea mood record."
    )
    return
  }

  if (!uiState.isLoading && uiState.logs.isEmpty() && uiState.totalLogCount > 0) {
    EmptyState(
      title = "No matching logs.",
      body = "Try changing or clearing filters."
    )
    return
  }

  LazyColumn(
    modifier = Modifier.padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    item {
      Text(
        text = "History",
        style = MaterialTheme.typography.headlineMedium
      )
    }
    item {
      WeeklyCaffeineCard(weekly = uiState.weeklyCaffeine)
    }
    item {
      HistoryFilterSection(
        isTodayOnly = uiState.isTodayOnly,
        selectedMood = uiState.selectedMoodFilter,
        selectedTime = uiState.selectedTimeFilter,
        selectedSortOrder = uiState.selectedSortOrder,
        currentCount = uiState.logs.size,
        totalCount = uiState.totalLogCount,
        filteredCaffeineTotalMg = uiState.filteredCaffeineTotalMg,
        onTodayOnlyChanged = onTodayOnlyChanged,
        onMoodFilterChanged = onMoodFilterChanged,
        onTimeFilterChanged = onTimeFilterChanged,
        onSortOrderChanged = onSortOrderChanged,
        onClearFilters = onClearFilters
      )
    }
    items(uiState.logs) { log ->
      LogRow(
        log = log,
        isDeleting = uiState.deletingLogId == log.id,
        onDeleteRequested = { pendingDeleteLogId = log.id }
      )
    }
  }
}

/*
 * Confirmation dialog before log deletion.
 */
@Composable
private fun DeleteConfirmDialog(
  onConfirm: () -> Unit,
  onDismiss: () -> Unit
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Delete log?") },
    text = { Text("This action cannot be undone.") },
    confirmButton = {
      TextButton(onClick = onConfirm) {
        Text("Delete")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancel")
      }
    }
  )
}

/*
 * Empty state for no logs or no filter matches.
 */
@Composable
private fun EmptyState(
  title: String,
  body: String
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .fillMaxHeight(),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = "$title\n$body",
      style = MaterialTheme.typography.titleMedium
    )
  }
}

/*
 * Filter chips for mood and time ranges.
 */
@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun HistoryFilterSection(
  isTodayOnly: Boolean,
  selectedMood: Mood?,
  selectedTime: TimeOfDay?,
  selectedSortOrder: HistorySortOrder,
  currentCount: Int,
  totalCount: Int,
  filteredCaffeineTotalMg: Int,
  onTodayOnlyChanged: (Boolean) -> Unit,
  onMoodFilterChanged: (Mood?) -> Unit,
  onTimeFilterChanged: (TimeOfDay?) -> Unit,
  onSortOrderChanged: (HistorySortOrder) -> Unit,
  onClearFilters: () -> Unit
) {
  Card {
    Column(
      modifier = Modifier.padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Filters",
          style = MaterialTheme.typography.titleMedium
        )
        TextButton(onClick = onClearFilters) {
          Text("Clear")
        }
      }
      Text(
        text = "Quick",
        style = MaterialTheme.typography.labelLarge
      )
      FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        FilterChip(
          selected = isTodayOnly,
          onClick = { onTodayOnlyChanged(!isTodayOnly) },
          label = { Text("Today only") }
        )
      }
      Text(
        text = "Mood",
        style = MaterialTheme.typography.labelLarge
      )
      FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        FilterChip(
          selected = selectedMood == null,
          onClick = { onMoodFilterChanged(null) },
          label = { Text("All") }
        )
        Mood.entries.forEach { mood ->
          FilterChip(
            selected = selectedMood == mood,
            onClick = { onMoodFilterChanged(mood) },
            label = { Text("${mood.emoji} ${mood.label}") }
          )
        }
      }
      Text(
        text = "Time",
        style = MaterialTheme.typography.labelLarge
      )
      FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        FilterChip(
          selected = selectedTime == null,
          onClick = { onTimeFilterChanged(null) },
          label = { Text("All") }
        )
        TimeOfDay.entries.forEach { time ->
          FilterChip(
            selected = selectedTime == time,
            onClick = { onTimeFilterChanged(time) },
            label = { Text(time.label) }
          )
        }
      }
      Text(
        text = "Sort",
        style = MaterialTheme.typography.labelLarge
      )
      FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        HistorySortOrder.entries.forEach { order ->
          FilterChip(
            selected = selectedSortOrder == order,
            onClick = { onSortOrderChanged(order) },
            label = { Text(order.label) }
          )
        }
      }
      Text(
        text = "Showing $currentCount / $totalCount logs",
        style = MaterialTheme.typography.bodySmall
      )
      Text(
        text = "Filtered caffeine total: ${filteredCaffeineTotalMg}mg",
        style = MaterialTheme.typography.bodySmall,
        fontWeight = FontWeight.SemiBold
      )
    }
  }
}

/*
 * Weekly caffeine graph card.
 */
@Composable
private fun WeeklyCaffeineCard(weekly: LinkedHashMap<LocalDate, Int>) {
  val max = weekly.values.maxOrNull()?.coerceAtLeast(1) ?: 1
  val dayFormat = DateTimeFormatter.ofPattern("E", Locale.getDefault())

  Card {
    Column(
      modifier = Modifier.padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Text(
        text = "Weekly Caffeine",
        style = MaterialTheme.typography.titleMedium
      )
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .height(140.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
      ) {
        weekly.forEach { (date, value) ->
          val ratio = value.toFloat() / max.toFloat()
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
              modifier = Modifier
                .height((100 * ratio).dp.coerceAtLeast(4.dp))
                .width(20.dp)
                .background(
                  color = MaterialTheme.colorScheme.primary,
                  shape = MaterialTheme.shapes.small
                )
            )
            Text(
              text = dayFormat.format(date),
              style = MaterialTheme.typography.bodySmall
            )
            Text(
              text = "${value}mg",
              style = MaterialTheme.typography.labelSmall
            )
          }
        }
      }
    }
  }
}

/*
 * Single log row item.
 */
@Composable
private fun LogRow(
  log: TeaLog,
  isDeleting: Boolean,
  onDeleteRequested: () -> Unit
) {
  val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
  Card {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
          text = "${log.mood.emoji} ${log.mood.label}",
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.SemiBold
        )
        Text(
          text = "${log.teaType.label} / ${log.timeOfDay.label}",
          style = MaterialTheme.typography.bodyMedium
        )
      }
      Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "${dateFormat.format(log.date)}\n${log.caffeineAmount}mg",
          style = MaterialTheme.typography.bodySmall
        )
        IconButton(
          onClick = onDeleteRequested,
          enabled = !isDeleting
        ) {
          Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete log"
          )
        }
      }
    }
  }
}
