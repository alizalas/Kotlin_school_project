package com.example.blank

import com.example.blank.telegrambot.model.HandlerName
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.bots.AbsSender

@Component
class QuizAnswerHandler : CallbackHandler {

    override val name: HandlerName = HandlerName.QUIZ_ANSWER

    override fun processCallbackData(
        absSender: AbsSender,
        callbackQuery: CallbackQuery,
        arguments: List<String>
    ) {

        val chatId = callbackQuery.message.chatId.toString()

        absSender.execute(
            EditMessageReplyMarkup(
                chatId,
                callbackQuery.message.messageId,
                callbackQuery.inlineMessageId,
                getInlineKeyboard(emptyList())
            )
        )

        if (arguments.first() == "b") {
            absSender.execute(createMessage(chatId, "Абсолютно верно!"))
        } else {
            absSender.execute(createMessage(chatId, "К сожалению, Вы ошиблись..."))
        }
    }
}