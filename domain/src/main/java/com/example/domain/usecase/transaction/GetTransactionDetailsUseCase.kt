package com.example.domain.usecase.transaction

import com.example.domain.model.TransactionDomain
import com.example.domain.repository.TransactionRepository
import com.example.domain.response.ApiResult
import javax.inject.Inject

class GetTransactionDetailsUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend fun execute(transactionId: Int) : ApiResult<TransactionDomain>{
        return repository.getTransactionDetails(transactionId)
    }
}