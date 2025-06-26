package com.example.domain.model

data class AccountHistoryItemDomain(
    val id: String,
    val accountId: String,
    val changeType: String,
    val previousState: AccountDomain?,
    val newState: AccountDomain,
    val changeTimestamp: String,
    val createdAt: String
)