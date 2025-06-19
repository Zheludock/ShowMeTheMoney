package com.example.showmethemoney.data.dto.transaction

import com.example.showmethemoney.domain.TransactionDomain

fun TransactionResponse.toDomain(): TransactionDomain {
    return TransactionDomain(
        id = id.toString(),
        accountId = account.id.toString(),
        categoryId = category.id.toString(),
        amount = amount,
        transactionDate = transactionDate,
        comment = comment,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}