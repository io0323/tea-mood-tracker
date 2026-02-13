package com.example.teamoodtracker.core

import com.example.teamoodtracker.data.repository.InMemoryTeaLogRepository
import com.example.teamoodtracker.data.repository.TeaLogRepository
import com.example.teamoodtracker.domain.usecase.AddTeaLogUseCase
import com.example.teamoodtracker.domain.usecase.GetAllLogsUseCase
import com.example.teamoodtracker.domain.usecase.GetTodayLogsUseCase
import com.example.teamoodtracker.domain.usecase.GetWeeklyCaffeineUseCase

/*
 * Lightweight dependency container for the app.
 */
class AppContainer {
  val teaLogRepository: TeaLogRepository = InMemoryTeaLogRepository()
  val getAllLogsUseCase = GetAllLogsUseCase(teaLogRepository)
  val getTodayLogsUseCase = GetTodayLogsUseCase(teaLogRepository)
  val addTeaLogUseCase = AddTeaLogUseCase(teaLogRepository)
  val getWeeklyCaffeineUseCase = GetWeeklyCaffeineUseCase()
}
