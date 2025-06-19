package com.example.showmethemoney.data.dto.transaction

import com.example.showmethemoney.data.dto.account.AccountInfo
import com.example.showmethemoney.data.dto.category.CategoryInfo

data class TransactionDetailsResponse(
    val id: Int,
    val account: AccountInfo,
    val category: CategoryInfo,
    val amount: String,
    val transactionDate: String,
    val comment: String?,
    val createdAt: String,
    val updatedAt: String
)