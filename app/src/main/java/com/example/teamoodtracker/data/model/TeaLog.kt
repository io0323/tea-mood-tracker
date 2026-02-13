package com.example.teamoodtracker.data.model

import java.time.LocalDate
import java.util.UUID

/*
 * Tea and mood log data model.
 */
data class TeaLog(
  val id: String = UUID.randomUUID().toString(),
  val date: LocalDate,
  val mood: Mood,
  val teaType: TeaType,
  val timeOfDay: TimeOfDay,
  val caffeineAmount: Int
)
