package com.example.data.dto.transaction

import com.example.domain.model.TransactionDomain

fun TransactionResponse.toDomain(): TransactionDomain {
    return TransactionDomain(
        id = id.toString(),
        emoji = category.emoji,
        categoryName = category.name,
        amount = amount,
        transactionDate = transactionDate,
        comment = comment,
        currency = account.currency,
        createdAt = createdAt,
        updatedAt = updatedAt,
        isIncome = category.isIncome
    )
}