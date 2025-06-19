package com.example.showmethemoney.domain

data class AccountHistoryDomain(
    val accountId: String,
    val accountName: String,
    val currency: String,
    val currentBalance: String,
    val history: List<AccountHistoryItemDomain>
)