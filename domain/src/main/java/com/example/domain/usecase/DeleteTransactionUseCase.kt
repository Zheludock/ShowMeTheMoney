package com.example.domain.usecase

import com.example.domain.repository.TransactionRepository
import com.example.domain.response.ApiResult
import javax.inject.Inject

class DeleteTransactionUseCase @Inject constructor(private val repository: TransactionRepository) {
    suspend fun execute(id: Int): ApiResult<Boolean>{
        return repository.deleteTransaction(id)
    }
}