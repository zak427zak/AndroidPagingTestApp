package com.testandroid.pagingtestapp.tools

import android.content.Context
import java.text.SimpleDateFormat
import java.util.*


class Converters(val context: Context?) {


    fun convertTimestamp(s: String, finishType: String): String? {
        val sdf: SimpleDateFormat
        return try {
            sdf = if (finishType == "short") {
                SimpleDateFormat("dd.MM.yyyy")
            } else {
                SimpleDateFormat("dd MMMM yyyy")
            }
            val netDate = Date(s.toLong() * 1000)
            sdf.format(netDate)
        } catch (e: Exception) {
            e.toString()
        }
    }

    fun pluralData(n: Int, what: String, needValue: Boolean, needDescription: Boolean): String {
        var days: List<String> = listOf()
        when (what) {
            "people" -> {
                days = listOf("человек добавил", "человека добавило", "человек добавили")
            }
            "flats" -> {
                days = listOf("квартира", "квартиры", "квартир")
            }
            "flat" -> {
                days = listOf("квартира", "квартиры", "квартир")
            }
            "parking" -> {
                days = listOf("машино-место", "машино-места", "машино-мест")
            }
            "minutes" -> {
                days = listOf("минута", "минуты", "минут")
            }
            "days" -> {
                days = listOf("день", "дня", "дней")
            }
            "offers" -> {
                days = listOf("предложение", "предложения", "предложений")
            }
            "lots" -> {
                days = listOf("лот", "лота", "лотов")
            }
            "added" -> {
                days = listOf("Добавлена", "Добавлены", "Добавлено")
            }
            "added_middle" -> {
                days = listOf("Добавлена", "Добавлено", "Добавлено")
            }
            "choosed" -> {
                days = listOf("Выбрана", "Выбраны", "Выбрано")
            }
            "founded" -> {
                days = listOf("Найдена", "Найдены", "Найдено")
            }
            "founded_middle" -> {
                days = listOf("Найдено", "Найдены", "Найдено")
            }
            "rooms" -> {
                days = listOf("комната", "комнаты", "комнат")
            }
            "months" -> {
                days = listOf("месяц", "месяца", "месяцев")
            }
        }

        val p: Int = when {
            (n % 10 == 1) and (n % 100 != 11) -> {
                0
            }
            ((2 <= n % 10) and (n % 10 <= 4)) and ((n % 100 < 10) or (n % 100 >= 20)) -> {
                1
            }
            else -> {
                2
            }
        }


        return if (needValue and needDescription) {
            "$n ${days[p]}"
        } else if (needValue) {
            "$n"
        } else {
            days[p]
        }

    }

}