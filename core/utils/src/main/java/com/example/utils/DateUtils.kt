package com.example.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * Утилитарный объект для работы с датами и их форматирования.
 *
 * Предоставляет методы для:
 * - Конвертации между строковыми и Date-представлениями дат
 * - Форматирования дат в различных форматах
 * - Получения специальных дат (начало месяца, текущая дата)
 *
 * ## Основные функции
 *
 * ### Форматирование дат
 * - [formatDate] - конвертирует Date в строку формата "yyyy-MM-dd"
 * - [formatTime] - конвертирует время из Date в строку формата "HH:mm"
 * - [formatDateForDisplay] - преобразует "yyyy-MM-dd" в "dd.MM.yyyy" для отображения
 *
 * ### Парсинг дат
 * - [stringToDate] - парсит строку в формате ISO 8601 ("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") в Date
 *
 * ### Специальные даты
 * - [getFirstDayOfCurrentMonth] - возвращает первый день текущего месяца
 * - [formatCurrentDate] - возвращает текущую дату в формате "yyyy-MM-dd"
 *
 * ## Особенности
 * - Для парсинга ISO-даты устанавливается UTC временная зона
 * - Форматы соответствуют стандартам:
 * - Внутренний: "yyyy-MM-dd" (ISO-подобный)
 * - Для отображения: "dd.MM.yyyy" (привычный формат)
 * - Время: "HH:mm" (24-часовой формат)
 */
object DateUtils {
    val utcFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    private val displayDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    private val displayTimeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    fun parseDate(dateString: String): Date? = try {
        utcFormat.parse(dateString)
    } catch (e: Exception) {
        null
    }

    fun formatToUtc(date: Date): String = utcFormat.format(date)

    fun formatDisplayDate(dateString: String): String = parseDate(dateString)?.let {
        displayDateFormat.format(it)
    } ?: dateString

    fun formatDisplayTime(dateString: String): String = parseDate(dateString)?.let {
        displayTimeFormat.format(it)
    } ?: dateString

    private val defaultLocale = Locale.getDefault()

    fun formatDate(date: Date): String {
        return SimpleDateFormat("yyyy-MM-dd", defaultLocale).format(date)
    }

    fun formatTime(date: Date): String {
        return SimpleDateFormat("HH:mm", defaultLocale).format(date)
    }

    fun stringToDate(dateString: String): Date? {
        return try {
            utcFormat.parse(dateString)
        } catch (e: Exception) {
            Log.e("DateUtils", "Failed to parse date: $dateString", e)
            null // Return null if parsing fails
        }
    }

    fun formatDateForDisplay(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", defaultLocale)
        val outputFormat = SimpleDateFormat("dd.MM.yyyy", defaultLocale)
        val date = inputFormat.parse(dateString)
        return outputFormat.format(date)
    }

    fun getFirstDayOfCurrentMonth(): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        return formatDate(calendar.time)
    }

    fun formatCurrentDate(): String {
        return formatDate(Date())
    }

    fun formatDateToIso8601startDay(inputDate: String?): String {
        val localDate = LocalDate.parse(inputDate, DateTimeFormatter.ISO_DATE)
        val zonedDateTime = localDate.atStartOfDay(ZoneOffset.UTC)
        return DateTimeFormatter.ISO_INSTANT.format(zonedDateTime)
    }

    fun formatDateToEndOfDayIso8601(inputDate: String?): String? {
        return try {
            val localDate = LocalDate.parse(inputDate, DateTimeFormatter.ISO_DATE)

            val endOfDay = localDate.atTime(LocalTime.MAX)

            DateTimeFormatter.ISO_INSTANT.format(endOfDay.atZone(ZoneOffset.UTC))
        } catch (e: Exception) {
            null
        }
    }
}