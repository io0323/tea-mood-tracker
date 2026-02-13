package com.example.teamoodtracker.domain.usecase

import com.example.teamoodtracker.data.model.TeaLog
import com.example.teamoodtracker.data.repository.TeaLogRepository
import java.time.LocalDate

/*
 * Use case for getting today's logs.
 */
class GetTodayLogsUseCase(
  private val repository: TeaLogRepository
) {
  /*
   * Return logs for current date.
   */
  operator fun invoke(today: LocalDate = LocalDate.now()): List<TeaLog> {
    return repository.getLogsByDate(today)
  }
}
