package com.example.showmethemoney.ui.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class TransactionUiState(
    val selectedAccountId: Int = 1,
    val accountName: String = "Сбербанк",
    val selectedCategoryId: String = "0",
    val categoryName: String = "",
    val amount: String = "",
    val transactionDate: Date = Date(),
    val comment: String = ""
) {
    fun getFormattedDate(): String {
        return SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(transactionDate)
    }

    fun getFormattedTime(): String {
        return SimpleDateFormat("HH:mm", Locale.getDefault()).format(transactionDate)
    }
}