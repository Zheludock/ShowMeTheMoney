package com.example.domain.usecase.transaction

import com.example.domain.model.CreateTransactionDomain
import com.example.domain.repository.TransactionRepository
import javax.inject.Inject

class CreateTransactionUseCase @Inject constructor(private val repository: TransactionRepository) {
    suspend fun execute(
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String? = null
    ): CreateTransactionDomain {
        return repository.createTransaction(
            accountId = accountId,
            categoryId = categoryId,
            amount = amount,
            transactionDate = transactionDate,
            comment = comment
        )
    }
}