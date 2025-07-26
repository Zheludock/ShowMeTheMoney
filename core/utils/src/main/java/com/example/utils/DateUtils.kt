package com.example.utils

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {
    fun dateToIsoString(date: Date): String {
        return date.toInstant()
            .atZone(ZoneOffset.UTC)
            .format(DateTimeFormatter.ISO_INSTANT)
    }
    fun isoStringToDate(isoString: String): Date {
        val instant = Instant.parse(isoString)
        return Date.from(instant)
    }
    fun startOfDay(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }
    fun endOfDay(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        return calendar.time
    }
    fun startOfDay29DaysAgo(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DAY_OF_YEAR, -29)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }
    fun formatDateToString(date: Date): String {
        val localDate = date.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        return localDate.format(formatter)
    }
    fun formatTime(date: Date): String {
        val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        return timeFormatter.format(date)
    }
    fun formatDateToBackend(date: Date): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return formatter.format(date)
    }
    fun getStartOfCurrentMonth(): Date {
        val startOfMonth = LocalDateTime.now()
            .withDayOfMonth(1)
            .withHour(0)
            .withMinute(0)
            .withSecond(0)
            .withNano(0)
        return Date.from(startOfMonth.atZone(ZoneId.systemDefault()).toInstant())
    }
}

