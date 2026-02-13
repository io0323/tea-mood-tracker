package com.example.teamoodtracker.domain.usecase

import com.example.teamoodtracker.data.model.TeaLog
import com.example.teamoodtracker.data.repository.TeaLogRepository
import kotlinx.coroutines.flow.StateFlow

/*
 * Use case for observing all logs.
 */
class GetAllLogsUseCase(
  private val repository: TeaLogRepository
) {
  /*
   * Return stream of all logs.
   */
  operator fun invoke(): StateFlow<List<TeaLog>> {
    return repository.observeLogs()
  }
}
