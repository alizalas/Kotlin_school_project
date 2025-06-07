package com.example.blank.telegrambot.command.trading

import com.example.blank.CommandName
import com.example.blank.DevmarkBot
import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.bots.AbsSender
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@Component
class CryptoCurrency : BotCommand(CommandName.TRADING.text, ""){

    private val scheduler = Executors.newSingleThreadScheduledExecutor()

    override fun execute(absSender: AbsSender, user: User, chat: Chat, arguments: Array<out String>) {
        val message = SendMessage()
        message.chatId = chat.id.toString()
        message.text = "Выберите валюту или криптовалюту:"

        val markup = InlineKeyboardMarkup()
        val row1 = listOf(
            InlineKeyboardButton("RUB/USD").apply { callbackData = "RUB/USD" },
            InlineKeyboardButton("RUB/EUR").apply { callbackData = "RUB/EUR" }
        )
        val row2 = listOf(
            InlineKeyboardButton("BTC").apply { callbackData = "BTC" },
            InlineKeyboardButton("ETH").apply { callbackData = "ETH" }
        )
        markup.keyboard = listOf(row1, row2)

        message.replyMarkup = markup

        try {
            absSender.execute(message)
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }
    }

    fun onCallback(absSender: DevmarkBot, update: Update) {
        val chatId = update.callbackQuery.message.chatId
        val data = update.callbackQuery.data

        sendRepeatedRate(absSender, chatId, data)
    }

    private fun sendRepeatedRate(absSender: AbsSender, chatId: Long, pair: String) {
        scheduler.scheduleAtFixedRate({
            val rate = fetchRate(pair)
            val message = SendMessage()
            message.chatId = chatId.toString()
            message.text = "Курс $pair: $rate"

            try {
                absSender.execute(message)
            } catch (e: TelegramApiException) {
                e.printStackTrace()
            }

        }, 0, 10, TimeUnit.SECONDS)
    }

    private fun fetchRate(pair: String): String {
        return when (pair) {
            "RUB/USD" -> HttpClient.getExchangeRate("RUB", "USD")?.toString() ?: "Ошибка"
            "RUB/EUR" -> HttpClient.getExchangeRate("RUB", "EUR")?.toString() ?: "Ошибка"
            "BTC" -> HttpClient.getCryptoRate("bitcoin")?.let { "$it USD" } ?: "Ошибка"
            "ETH" -> HttpClient.getCryptoRate("ethereum")?.let { "$it USD" } ?: "Ошибка"
            else -> "Неизвестная валюта"
        }
    }

}