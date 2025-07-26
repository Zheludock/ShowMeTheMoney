package com.example.data.room.entityes

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "accounts",
    indices = [Index("id")])
data class AccountEntity(
    @PrimaryKey val id: Int,
    val userId: Int,
    val name: String,
    val balance: String,
    val currency: String,
    val createdAt: Date,
    val updatedAt: Date?
)