package com.example.domain.usecase

import com.example.domain.ApiResult
import com.example.domain.model.TransactionDomain
import com.example.domain.repository.TransactionRepository
import javax.inject.Inject

class GetTransactionsUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend fun execute(
        accountId: String,
        startDate: String,
        endDate: String
    ): ApiResult<List<TransactionDomain>> {
        return repository.getTransactions(accountId.toInt(), startDate, endDate)
    }
}