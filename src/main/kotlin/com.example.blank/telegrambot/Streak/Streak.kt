package com.example.blank

import java.time.LocalDate

data class Streak(
    val userId: Long,
    val currentStreak: Int = 0,
    val lastActivityDate: LocalDate = LocalDate.now(),
    val longestStreak: Int = 0
)