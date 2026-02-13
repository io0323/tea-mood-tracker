package com.example.teamoodtracker.domain.usecase

import com.example.teamoodtracker.data.model.TeaLog
import com.example.teamoodtracker.data.repository.TeaLogRepository

/*
 * Use case for adding a tea log.
 */
class AddTeaLogUseCase(
  private val repository: TeaLogRepository
) {
  /*
   * Persist a new log.
   */
  operator fun invoke(log: TeaLog) {
    repository.addLog(log)
  }
}
