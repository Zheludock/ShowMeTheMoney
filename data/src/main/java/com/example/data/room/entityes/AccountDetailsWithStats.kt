package com.example.data.room.entityes

import androidx.room.Embedded
import androidx.room.Relation

data class AccountDetailsWithStats(
    @Embedded val details: AccountDetailsEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "accountId",
        entity = CategoryStatsEntity::class
    )
    val stats: List<CategoryStatsEntity>
)