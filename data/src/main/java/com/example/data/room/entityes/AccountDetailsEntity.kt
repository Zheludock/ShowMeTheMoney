package com.example.data.room.entityes

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "account_details")
data class AccountDetailsEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val balance: String,
    val currency: String,
    val createdAt: Date,
    val updatedAt: Date?
)