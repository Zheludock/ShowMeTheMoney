package com.example.domain.usecase.transaction

import com.example.domain.model.TransactionDomain
import com.example.domain.model.TransactionInput
import com.example.domain.repository.TransactionRepository
import com.example.domain.response.ApiResult
import javax.inject.Inject

class UpdateTransactionUseCase @Inject constructor(private val repository: TransactionRepository) {
    suspend fun execute(id: Int,
                        accountId: Int,
                        categoryId: Int,
                        amount: String,
                        date: String,
                        comment: String?): ApiResult<TransactionDomain> {
        val transactionInput = TransactionInput(
            transactionId = id,
            accountId = accountId,
            categoryId = categoryId,
            amount = amount,
            transactionDate = date,
            comment = comment
        )

        return repository.updateTransaction(transactionInput)
    }
}