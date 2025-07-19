package com.example.data.room.entityes

import androidx.room.Embedded
import androidx.room.Relation

data class AccountHistoryWithItems(
    @Embedded val history: AccountHistoryEntity,
    @Relation(
        parentColumn = "accountId",
        entityColumn = "accountId"
    )
    val items: List<AccountHistoryItemEntity>
)