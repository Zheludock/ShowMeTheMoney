package com.example.data.room.entityes

import androidx.room.Embedded
import androidx.room.Relation

data class TransactionWithCategoryAndAccount(
    @Embedded val transaction: TransactionEntity,

    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val category: CategoryEntity,

    @Relation(
        parentColumn = "accountId",
        entityColumn = "id"
    )
    val account: AccountEntity
)