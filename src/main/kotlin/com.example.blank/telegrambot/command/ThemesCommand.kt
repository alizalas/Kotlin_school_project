package com.example.blank

import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender


@Component
class ThemesCommand : BotCommand(CommandName.THEMES.text, "Выбор темы для взаимодействия") {

    override fun execute(absSender: AbsSender, user: User, chat: Chat, arguments: Array<out String>) {

        absSender.execute(createMessage(chat.id.toString(),
            """
                🌿 *Выберите тему*
                Пожалуйста, выберите интересующую вас категорию:
                
                • Образование 🎓 /education - учебные материалы
                • Технологии 💻 /technology - IT и инновации
                • Спорт 🤸 /sport - Активности
                • Искусство 🎭 /art - Мировая художножественная культура
                • Психология 📖 /psichology - Анализ себя и окружающих
            """))


    }
}