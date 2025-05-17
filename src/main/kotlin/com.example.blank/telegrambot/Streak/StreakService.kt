package com.example.blank

import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Service
class StreakService {
    private val userStreaks = mutableMapOf<Long, Streak>()

    fun updateStreak(userId: Long): Streak {
        val today = LocalDate.now()
        val currentStreak = userStreaks[userId] ?: Streak(userId)

        return if (currentStreak.lastActivityDate.isBefore(today)) {
            val daysBetween = ChronoUnit.DAYS.between(currentStreak.lastActivityDate, today).toInt()

            val newStreak = when {
                daysBetween == 1 -> currentStreak.copy(
                    currentStreak = currentStreak.currentStreak + 1,
                    lastActivityDate = today,
                    longestStreak = maxOf(currentStreak.longestStreak, currentStreak.currentStreak + 1)
                )
                daysBetween > 1 -> currentStreak.copy(
                    currentStreak = 1,
                    lastActivityDate = today
                )
                else -> currentStreak
            }

            userStreaks[userId] = newStreak
            newStreak
        } else {
            currentStreak
        }
    }

    fun getStreak(userId: Long): Streak {
        return userStreaks[userId] ?: Streak(userId)
    }
}