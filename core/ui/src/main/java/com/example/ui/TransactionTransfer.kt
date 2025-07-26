package com.example.ui

import com.example.domain.model.TransactionDomain

object TransactionTransfer {
    lateinit var editedTransaction: TransactionItem

    fun toDomain(): TransactionDomain{
        return TransactionDomain(
            id = editedTransaction.id,
            emoji = editedTransaction.categoryEmoji,
            categoryName = editedTransaction.categoryName,
            categoryId = editedTransaction.categoryId,
            amount = editedTransaction.amount,
            transactionDate = editedTransaction.transactionDate,
            comment = editedTransaction.comment,
            currency = editedTransaction.accountCurrency,
            isIncome = editedTransaction.isIncome
        )
    }
}