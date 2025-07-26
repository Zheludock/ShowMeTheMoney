package com.example.data.room.entityes

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "transactions",
    indices = [Index("id")],
    foreignKeys = [
        ForeignKey(
        entity = AccountEntity::class,
        parentColumns = ["id"],
        childColumns = ["accountId"]
    ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"]
        )]
)
data class TransactionEntity(
    @PrimaryKey val id: Int,
    val accountId: Int,
    val categoryId: Int,
    val amount: String,
    val comment: String?,
    val transactionDate: Date,
    val createdAt: Date,
    val updatedAt: Date?,
    val pendingSync: Boolean = false,
    val isDeleted: Boolean = false
)