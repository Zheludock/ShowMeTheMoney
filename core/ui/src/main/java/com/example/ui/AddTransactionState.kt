package com.example.ui

import com.example.utils.AccountManager
import com.example.utils.DateUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class AddTransactionState(
    val accountId: Int = AccountManager.selectedAccountId,
    val accountName: String = "",
    val categoryId: Int = -1,
    val categoryName: String = "",
    val amount: String = "",
    val transactionDate: String = DateUtils.formatCurrentDate(),
    val displayDate: String = "",
    val displayTime: String = "",
    val comment: String? = null,
) {
    fun getFormattedDate(): String {
        val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return sdf.format(Date(transactionDate))
    }

    fun getFormattedTime(): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(Date(transactionDate))
    }
}