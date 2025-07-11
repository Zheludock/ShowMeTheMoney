package com.example.domain.usecase

import com.example.domain.model.TransactionDomain
import com.example.domain.repository.TransactionRepository
import com.example.domain.response.ApiResult
import javax.inject.Inject

class CreateTransactionUseCase @Inject constructor(private val repository: TransactionRepository) {
    suspend fun execute(
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String? = null
    ): ApiResult<TransactionDomain> {
        return repository.createTransaction(
            accountId = accountId,
            categoryId = categoryId,
            amount = amount,
            transactionDate = transactionDate,
            comment = comment
        )
    }
}