package com.example.showmethemoney.data.dto.account

data class AccountHistoryResponse(
    val accountId: Int,
    val accountName: String,
    val currency: String,
    val currentBalance: String,
    val history: List<AccountHistoryItem>
)