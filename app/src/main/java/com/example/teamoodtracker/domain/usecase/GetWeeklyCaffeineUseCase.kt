package com.example.teamoodtracker.domain.usecase

import com.example.teamoodtracker.data.model.TeaLog
import java.time.LocalDate

/*
 * Use case to aggregate weekly caffeine values.
 */
class GetWeeklyCaffeineUseCase {
  /*
   * Return map from day to caffeine sum in the last 7 days.
   */
  operator fun invoke(
    logs: List<TeaLog>,
    today: LocalDate = LocalDate.now()
  ): LinkedHashMap<LocalDate, Int> {
    val start = today.minusDays(6)
    val baseMap = (0L..6L).associate { day ->
      val date = start.plusDays(day)
      date to 0
    }.toMutableMap()

    logs.filter { it.date in start..today }.forEach { log ->
      val current = baseMap[log.date] ?: 0
      baseMap[log.date] = current + log.caffeineAmount
    }

    return LinkedHashMap(baseMap.toSortedMap())
  }
}
