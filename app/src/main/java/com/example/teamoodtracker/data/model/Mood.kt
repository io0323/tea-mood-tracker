package com.example.teamoodtracker.data.model

/*
 * User mood options tracked by the app.
 */
enum class Mood(
  val label: String,
  val emoji: String
) {
  CALM("Calm", "🙂"),
  HAPPY("Happy", "😄"),
  TIRED("Tired", "😴"),
  STRESSED("Stressed", "😣")
}
