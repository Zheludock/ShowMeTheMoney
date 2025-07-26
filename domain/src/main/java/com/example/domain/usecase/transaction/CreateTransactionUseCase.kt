package com.example.domain.usecase.transaction

import com.example.domain.repository.TransactionRepository
import java.util.Date
import javax.inject.Inject

class CreateTransactionUseCase @Inject constructor(private val repository: TransactionRepository) {
    suspend fun execute(
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: Date,
        comment: String? = null
    ) {
        repository.createTransaction(
            accountId = accountId,
            categoryId = categoryId,
            amount = amount,
            transactionDate = transactionDate,
            comment = comment
        )
    }
}