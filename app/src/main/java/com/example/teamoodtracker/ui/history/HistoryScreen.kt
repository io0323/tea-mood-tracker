package com.example.teamoodtracker.ui.history

import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teamoodtracker.data.model.TeaLog
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/*
 * Route composable for the History screen.
 */
@Composable
fun HistoryRoute(viewModel: HistoryViewModel) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  HistoryScreen(uiState = uiState)
}

/*
 * Main history UI including graph and list.
 */
@Composable
fun HistoryScreen(uiState: HistoryUiState) {
  if (!uiState.isLoading && uiState.logs.isEmpty()) {
    EmptyState()
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
    items(uiState.logs) { log ->
      LogRow(log = log)
    }
  }
}

/*
 * Empty state for no logs.
 */
@Composable
private fun EmptyState() {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .fillMaxHeight(),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = "No logs yet.\nAdd your first tea mood record.",
      style = MaterialTheme.typography.titleMedium
    )
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
private fun LogRow(log: TeaLog) {
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
      Text(
        text = "${dateFormat.format(log.date)}\n${log.caffeineAmount}mg",
        style = MaterialTheme.typography.bodySmall
      )
    }
  }
}
