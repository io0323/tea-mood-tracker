package com.example.teamoodtracker.data.repository

import com.example.teamoodtracker.data.model.Mood
import com.example.teamoodtracker.data.model.TeaLog
import com.example.teamoodtracker.data.model.TeaType
import com.example.teamoodtracker.data.model.TimeOfDay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate

/*
 * In-memory repository for simulator-only usage.
 */
class InMemoryTeaLogRepository : TeaLogRepository {
  private val logsState = MutableStateFlow(seedLogs())

  /*
   * Observe current logs.
   */
  override fun observeLogs(): StateFlow<List<TeaLog>> = logsState.asStateFlow()

  /*
   * Filter logs by date.
   */
  override fun getLogsByDate(date: LocalDate): List<TeaLog> {
    return logsState.value.filter { it.date == date }
  }

  /*
   * Add log and keep date-descending order.
   */
  override fun addLog(log: TeaLog) {
    val updated = (logsState.value + log).sortedByDescending { it.date }
    logsState.value = updated
  }

  /*
   * Seed data for initial UI verification.
   */
  private fun seedLogs(): List<TeaLog> {
    val today = LocalDate.now()
    return listOf(
      TeaLog(
        date = today,
        mood = Mood.CALM,
        teaType = TeaType.GREEN_TEA,
        timeOfDay = TimeOfDay.MORNING,
        caffeineAmount = TeaType.GREEN_TEA.caffeineMg
      ),
      TeaLog(
        date = today.minusDays(1),
        mood = Mood.HAPPY,
        teaType = TeaType.BLACK_TEA,
        timeOfDay = TimeOfDay.AFTERNOON,
        caffeineAmount = TeaType.BLACK_TEA.caffeineMg
      ),
      TeaLog(
        date = today.minusDays(2),
        mood = Mood.TIRED,
        teaType = TeaType.OOLONG,
        timeOfDay = TimeOfDay.EVENING,
        caffeineAmount = TeaType.OOLONG.caffeineMg
      )
    )
  }
}
