package com.example.data.room.entityes

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "category_stats",
    foreignKeys = [ForeignKey(
        entity = AccountDetailsEntity::class,
        parentColumns = ["id"],
        childColumns = ["accountId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("accountId")]
)
data class CategoryStatsEntity(
    @PrimaryKey(autoGenerate = true) val localId: Int = 0,
    val accountId: Int,
    val categoryId: Int,
    val categoryName: String,
    val emoji: String,
    val amount: String,
    val isIncome: Boolean
)