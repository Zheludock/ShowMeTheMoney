package com.example.showmethemoney.ui.utils

import com.example.domain.model.CategoryDomain
import com.example.domain.model.TransactionDomain

fun TransactionDomain.toTransactionItem(): TransactionItem{
    return TransactionItem(
        id = id,
        categoryEmoji = emoji,
        categoryName = categoryName,
        comment = comment,
        amount = amount,
        accountCurrency = currency,
        createdAt = createdAt,
        isIncome = isIncome
        )
}

fun CategoryDomain.toCategoryItem(): CategoryItem{
    return CategoryItem(
        isIncome = isIncome,
        id = categoryId,
        emoji = emoji,
        name = categoryName
    )
}
