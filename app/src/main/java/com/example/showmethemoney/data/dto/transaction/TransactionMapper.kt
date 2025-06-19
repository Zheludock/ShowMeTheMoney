package com.example.showmethemoney.data.dto.transaction

import com.example.showmethemoney.domain.ExpensesDomain

fun TransactionResponse.toDomain(): ExpensesDomain {
    return ExpensesDomain(
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

fun TransactionDetailsResponse.toDomain(): ExpensesDomain {
    return ExpensesDomain(
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