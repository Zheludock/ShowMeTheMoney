package com.example.data.room.entityes

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "account_history_item",
    foreignKeys = [ForeignKey(
        entity = AccountHistoryEntity::class,
        parentColumns = ["accountId"],
        childColumns = ["accountId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("accountId")]
)
data class AccountHistoryItemEntity(
    @PrimaryKey val id: Int,
    val accountId: Int,
    val changeType: String,
    val previousStateJson: String?,
    val newStateJson: String,
    val changeTimestamp: Date,
    val createdAt: Date
)