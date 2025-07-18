package com.example.ui

import com.example.utils.AccountManager
import com.example.utils.DateUtils
import java.text.SimpleDateFormat
import java.util.Locale

data class AddTransactionState(
    val accountId: Int = AccountManager.selectedAccountId,
    val accountName: String = AccountManager.selectedAccountName.value,
    val categoryId: Int = -1,
    val categoryName: String = "",
    val amount: String = "",
    val transactionDate: String = DateUtils.formatCurrentDate(),
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