package com.example.data.room.entityes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account_history")
data class AccountHistoryEntity(
    @PrimaryKey val accountId: Int,
    val accountName: String,
    val currency: String,
    val currentBalance: String
)
