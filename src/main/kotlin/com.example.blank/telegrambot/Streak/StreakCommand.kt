package com.example.blank.telegrambot.Streak

import com.example.blank.StreakService
import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender

@Component
class StrickCommand(
    private val streakService: StreakService
) : BotCommand("/strick", "Проверить текущий стрик") {

    override fun execute(absSender: AbsSender, user: User, chat: Chat, arguments: Array<out String>) {
        val streak = streakService.getStreak(user.id)

        val messageText = buildString {
            append("\uD83D\uDD25 *Твой стрик:*\n\n")

            when {
                streak.currentStreak == 0 -> append("\uD83D\uDD53 У тебя еще нет активного стрика!\n")
                streak.currentStreak >= 7 -> append("\uD83D\uDD25\uD83D\uDD25\uD83D\uDD25 Огненный стрик!\n")
                else -> append("\uD83D\uDD52 Ты на верном пути!\n")
            }

            append("\n\uD83D\uDCC5 Текущий: ${streak.currentStreak} дней")
            append("\n\uD83C\uDFC6 Рекорд: ${streak.longestStreak} дней")

            if (streak.currentStreak > 0) {
                append("\n\n\uD83D\uDCC6 Последняя активность: ${streak.lastActivityDate}")
            }

            append("\n\nВозвращайся завтра, чтобы продолжить стрик!")
        }

        val message = SendMessage(chat.id.toString(), messageText).apply {
            enableMarkdown(true)
        }
        absSender.execute(message)
    }
}