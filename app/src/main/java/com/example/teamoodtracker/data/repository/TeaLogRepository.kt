package com.example.teamoodtracker.data.repository

import com.example.teamoodtracker.data.model.TeaLog
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate

/*
 * Repository contract for tea logs.
 */
interface TeaLogRepository {
  /*
   * Observe all logs as a state stream.
   */
  fun observeLogs(): StateFlow<List<TeaLog>>

  /*
   * Return logs on a specific date.
   */
  fun getLogsByDate(date: LocalDate): List<TeaLog>

  /*
   * Add a new tea log.
   */
  fun addLog(log: TeaLog)
}
