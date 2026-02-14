package com.example.teamoodtracker.domain.usecase

import com.example.teamoodtracker.data.repository.TeaLogRepository

/*
 * Use case for deleting a tea log.
 */
class DeleteTeaLogUseCase(
  private val repository: TeaLogRepository
) {
  /*
   * Remove a log by identifier.
   */
  operator fun invoke(logId: String) {
    repository.deleteLog(logId)
  }
}
