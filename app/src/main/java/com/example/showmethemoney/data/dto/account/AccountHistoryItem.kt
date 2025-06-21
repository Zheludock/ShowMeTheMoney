package com.example.showmethemoney.data.dto.account

data class AccountHistoryItem(
    val id: Int,
    val accountId: Int,
    val changeType: String,
    val previousState: AccountInfo?,
    val newState: AccountInfo,
    val changeTimestamp: String,
    val createdAt: String
)