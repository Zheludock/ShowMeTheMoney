package com.example.transactions.addtransaction

import com.example.utils.AccountManager
import com.example.utils.DateUtils

data class TransactionUiState(
    val selectedAccountId: Int = AccountManager.selectedAccountId,
    val accountName: String,
    val selectedCategoryId: Int,
    val categoryName: String,
    val amount: String,
    val transactionDate: String,
    val comment: String?
) {
    fun getFormattedDate(): String = DateUtils.formatDisplayDate(transactionDate)
    fun getFormattedTime(): String = DateUtils.formatDisplayTime(transactionDate)
}
