package com.example.blank



import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
class StartCommand(private val streakService: StreakService) : BotCommand(CommandName.START.text, "") {
    private val adminChatId = "1995205565"

    override fun execute(absSender: AbsSender, user: User, chat: Chat, arguments: Array<out String>) {

        val streak = streakService.updateStreak(user.id)

        absSender.execute(createWelcomeMessage(chat.id.toString(), streak))
        sendAdminNotification(absSender, user, chat, streak)
    }
    private fun createWelcomeMessage(chatId: String, streak: Streak): SendMessage {
        return SendMessage(chatId, """
            🌱 Добро пожаловать в RootsUp!
            
            🔥 Твой текущий стрик: ${streak.currentStreak} дней
            🏆 Рекорд: ${streak.longestStreak} дней
            
            Твой помощник в саморазвитии. Вот что я умею:
            
            📌 Основные команды:
            /theme – темы
            /strick – проверить стрик
            /quiz - квизы
            /habits – трекер привычек
            /goals – постановка целей
            /challenge – челлендж дня
            /friends – друзья
            
            Возвращайся каждый день, чтобы увеличить стрик!
            
            «Маленькие шаги каждый день — большие изменения в итоге» ✨
        """.trimIndent())
    }
    private fun sendAdminNotification(
        absSender: AbsSender,
        user: User,
        chat: Chat,
        streak: Streak
    ) {
        val currentTime = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"))

        // Экранируем специальные символы MarkdownV2
        val escapedFirstName = user.firstName.replace(Regex("([_*\\[\\]()~`>#+=|{}.!-]"), "\\\\$1")
        val escapedLastName = user.lastName?.replace(Regex("([_*\\[\\]()~`>#+=|{}.!-]"), "\\\\$1") ?: "не указана"
        val escapedUsername = user.userName?.replace(Regex("([_*\\[\\]()~`>#+=|{}.!-]"), "\\\\$1") ?: "не указан"

        val notificationText = """
        *📢 Новый пользователь бота\!*
        
        *👤 Информация:*
        ├ Имя: ${escapedFirstName}
        ├ Фамилия: ${escapedLastName}
        ├ Логин: @${escapedUsername}
        ├ ID: `${user.id}`
        ├ Стрик: ${streak.currentStreak} дней
        └ Время: ${currentTime.replace("-", "\\-")}
        
        [✉️ Написать пользователю](tg://user?id=${user.id})
    """.trimIndent()

        try {
            val notification = SendMessage.builder()
                .chatId(adminChatId)
                .text(notificationText)
                .parseMode("MarkdownV2")
                .disableWebPagePreview(true)
                .build()

            absSender.execute(notification)
        } catch (e: Exception) {
            System.err.println("Ошибка отправки уведомления админу: ${e.message}")
            e.printStackTrace()
        }
    }
}