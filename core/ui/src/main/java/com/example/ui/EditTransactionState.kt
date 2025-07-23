package com.example.ui

import com.example.utils.AccountManager
import java.util.Date

data class EditTransactionState(
    val id: Int = -1,
    val accountId: Int = AccountManager.selectedAccountId,
    val accountName: String = AccountManager.selectedAccountName.value,
    val categoryId: Int = -1,
    val categoryName: String = "",
    val amount: String = "",
    val transactionDate: Date = Date(),
    val comment: String? = null,
)