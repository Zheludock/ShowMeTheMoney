package com.example.ui

import com.example.utils.AccountManager
import java.text.SimpleDateFormat
import java.util.Locale

data class EditTransactionState(
    val id: Int = -1,
    val accountId: Int = AccountManager.selectedAccountId,
    val accountName: String = AccountManager.selectedAccountName.value,
    val categoryId: Int = -1,
    val categoryName: String = "",
    val amount: String = "",
    val transactionDate: String = "",
    val displayDate: String = "",
    val displayTime: String = "",
    val comment: String? = null,
) {
    fun getFormattedDate(): String {
        return try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val date = dateFormat.parse(transactionDate)
            SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(date)
        } catch (e: Exception) {
            transactionDate
        }
    }

    fun getFormattedTime(): String {
        return try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val date = dateFormat.parse(transactionDate)
            SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)
        } catch (e: Exception) {
            transactionDate
        }
    }
}