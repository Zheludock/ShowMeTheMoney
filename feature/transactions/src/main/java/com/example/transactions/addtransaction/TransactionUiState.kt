package com.example.transactions.addtransaction

import com.example.ui.AccountManager
import java.text.SimpleDateFormat
import java.util.Locale

data class TransactionUiState(
    val selectedAccountId: Int = AccountManager.selectedAccountId,
    var accountName: String,
    var selectedCategoryId: Int,
    var categoryName: String,
    var amount: String,
    var transactionDate: String,
    var comment: String?
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