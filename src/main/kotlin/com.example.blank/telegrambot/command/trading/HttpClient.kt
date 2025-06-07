package com.example.blank.telegrambot.command.trading

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import okhttp3.OkHttpClient
import okhttp3.Request

object HttpClient {
    private val client = OkHttpClient()
    private val mapper = jacksonObjectMapper()

    fun getExchangeRate(from: String, to: String): Double? {
        val url = "https://api.exchangerate.host/latest?base=$from&symbols=$to"
        val request = Request.Builder().url(url).build()
        client.newCall(request).execute().use { response ->
            val json = response.body?.string()
            val tree = mapper.readTree(json)
            return tree["rates"]?.get(to)?.asDouble()
        }
    }

    fun getCryptoRate(cryptoId: String): Double? {
        val url = "https://api.coingecko.com/api/v3/simple/price?ids=$cryptoId&vs_currencies=usd"
        val request = Request.Builder().url(url).build()
        client.newCall(request).execute().use { response ->
            val json = response.body?.string()
            val tree = mapper.readTree(json)
            return tree[cryptoId]?.get("usd")?.asDouble()
        }
    }
}