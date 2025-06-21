package com.example.showmethemoney.ui.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun formatDate(date: Date): String {
    return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
}
fun formatTime(date: Date): String{
    return SimpleDateFormat("HH:MM", Locale.getDefault()).format(date)
}
fun stringToDate(
    dateString: String
): Date? {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    format.timeZone = TimeZone.getTimeZone("UTC")
    return format.parse(dateString)
}

fun formatDateForDisplay(dateString: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    val date = inputFormat.parse(dateString)
    return outputFormat.format(date)
}

fun formatDateForBackend(dateString: String): String{
    val inputFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val date = inputFormat.parse(dateString)
    return outputFormat.format(date)
}