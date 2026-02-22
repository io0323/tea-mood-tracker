package com.example.teamoodtracker.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teamoodtracker.data.model.TimeOfDay
import com.example.teamoodtracker.ui.common.toAccentColor

/*
 * Route composable for the Today screen.
 */
@Composable
fun HomeRoute(viewModel: HomeViewModel) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  HomeScreen(uiState = uiState)
}

/*
 * Main Today UI layout.
 */
@Composable
fun HomeScreen(uiState: HomeUiState) {
  val hasData = uiState.todayLogs.isNotEmpty()
  LazyColumn(
    modifier = Modifier.padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    item {
      Text(
        text = "Today",
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.SemiBold
      )
    }

    item {
      AnimatedVisibility(
        visible = hasData,
        enter = fadeIn()
      ) {
        TodayMoodCard(
          moodText = uiState.dominantMood?.let {
            "${it.emoji} ${it.label}"
          } ?: "No mood yet",
          accent = uiState.dominantMood?.toAccentColor()
            ?: MaterialTheme.colorScheme.primary
        )
      }
    }

    item {
      TeaListCard(
        teaLabels = uiState.teasToday.map { teaType -> teaType.label }
      )
    }

    item {
      TodayCountCard(
        logsCountToday = uiState.logsCountToday,
        timeOfDayCount = uiState.timeOfDayCount
      )
    }

    item {
      CaffeineCard(caffeineTodayMg = uiState.caffeineTodayMg)
    }
    item {
      WeeklyCaffeineSummaryCard(
        weeklyTotalMg = uiState.weeklyCaffeineTotalMg,
        weeklyAverageMg = uiState.weeklyCaffeineAverageMg
      )
    }
  }
}

/*
 * Card showing total logs and time-bucket summary.
 */
@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun TodayCountCard(
  logsCountToday: Int,
  timeOfDayCount: Map<TimeOfDay, Int>
) {
  Card {
    Column(
      modifier = Modifier.padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Text(
        text = "Today's Records",
        style = MaterialTheme.typography.titleMedium
      )
      Text(
        text = "$logsCountToday logs",
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.SemiBold
      )
      FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        TimeOfDay.entries.forEach { time ->
          val count = timeOfDayCount[time] ?: 0
          AssistChip(
            onClick = {},
            enabled = false,
            label = { Text("${time.label}: $count") }
          )
        }
      }
    }
  }
}

/*
 * Card showing current mood.
 */
@Composable
private fun TodayMoodCard(
  moodText: String,
  accent: androidx.compose.ui.graphics.Color
) {
  Card(
    colors = CardDefaults.cardColors(containerColor = accent.copy(alpha = 0.15f))
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Text(
        text = "Today's Mood",
        style = MaterialTheme.typography.titleMedium
      )
      Text(
        modifier = Modifier.padding(top = 8.dp),
        text = moodText,
        style = MaterialTheme.typography.headlineSmall
      )
    }
  }
}

/*
 * Card showing tea names consumed today.
 */
@Composable
private fun TeaListCard(teaLabels: List<String>) {
  Card {
    Column(modifier = Modifier.padding(16.dp)) {
      Text(
        text = "Tea Intake",
        style = MaterialTheme.typography.titleMedium
      )
      val content = if (teaLabels.isEmpty()) {
        "No tea logged yet."
      } else {
        teaLabels.joinToString(separator = " / ")
      }
      Text(
        modifier = Modifier.padding(top = 8.dp),
        text = content,
        style = MaterialTheme.typography.bodyLarge
      )
    }
  }
}

/*
 * Card showing caffeine amount and gauge.
 */
@Composable
private fun CaffeineCard(caffeineTodayMg: Int) {
  val goal = 200f
  val progress = (caffeineTodayMg / goal).coerceIn(0f, 1f)
  Card {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(
          text = "Estimated Caffeine",
          style = MaterialTheme.typography.titleMedium
        )
        Text(
          text = "${caffeineTodayMg}mg",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold
        )
      }
      LinearProgressIndicator(
        progress = { progress },
        modifier = Modifier.fillMaxWidth()
      )
    }
  }
}

/*
 * Card showing total and average caffeine for last week.
 */
@Composable
private fun WeeklyCaffeineSummaryCard(
  weeklyTotalMg: Int,
  weeklyAverageMg: Int
) {
  Card {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Text(
        text = "Last 7 Days",
        style = MaterialTheme.typography.titleMedium
      )
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(
          text = "Total caffeine",
          style = MaterialTheme.typography.bodyMedium
        )
        Text(
          text = "${weeklyTotalMg}mg",
          style = MaterialTheme.typography.bodyMedium,
          fontWeight = FontWeight.SemiBold
        )
      }
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(
          text = "Daily average",
          style = MaterialTheme.typography.bodyMedium
        )
        Text(
          text = "${weeklyAverageMg}mg/day",
          style = MaterialTheme.typography.bodyMedium,
          fontWeight = FontWeight.SemiBold
        )
      }
    }
  }
}
