package com.example.teamoodtracker.ui.navigation

/*
 * Navigation destinations for TeaMood Tracker.
 */
sealed class TeaMoodDestination(
  val route: String,
  val label: String
) {
  data object Today : TeaMoodDestination("today", "Today")
  data object AddLog : TeaMoodDestination("add_log", "Add")
  data object History : TeaMoodDestination("history", "History")
}
