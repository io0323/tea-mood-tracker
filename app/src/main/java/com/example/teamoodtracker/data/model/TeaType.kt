package com.example.teamoodtracker.data.model

/*
 * Tea type with estimated caffeine value.
 */
enum class TeaType(
  val label: String,
  val caffeineMg: Int
) {
  GREEN_TEA("Green Tea", 35),
  BLACK_TEA("Black Tea", 47),
  OOLONG("Oolong", 38),
  HERBAL("Herbal", 0)
}
