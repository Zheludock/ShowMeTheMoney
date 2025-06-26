package com.example.data

import com.example.data.dto.transaction.CreateTransactionRequest
import com.example.data.dto.transaction.toDomain
import com.example.data.safecaller.ApiCallHelper
import com.example.domain.ApiResult
import com.example.domain.model.TransactionDomain
import com.example.domain.model.TransactionInput
import com.example.domain.repository.TransactionRepository
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val apiService: TransactionApiService,
    private val apiCallHelper: ApiCallHelper
) : TransactionRepository {
    override suspend fun getTransactions(
        accountId: Int,
        startDate: String?,
        endDate: String?
    ): ApiResult<List<TransactionDomain>> {
        return apiCallHelper.safeApiCall(block =
            {apiService.getTransactions(accountId, startDate, endDate).map { it.toDomain() }}
        )
    }

    override suspend fun createTransaction(
        accountId: Int,
        categoryId: String,
        amount: String,
        transactionDate: String,
        comment: String?
    ): ApiResult<TransactionDomain> {
        val request = CreateTransactionRequest(
            accountId = accountId,
            categoryId = categoryId.toInt(),
            amount = amount,
            transactionDate = transactionDate,
            comment = comment
        )
        return apiCallHelper.safeApiCall(block = {
            apiService.createTransaction(request).toDomain()
        })
    }

    override suspend fun getTransactionDetails(transactionId: Int): ApiResult<TransactionDomain> {
        return apiCallHelper.safeApiCall(block = {
            apiService.getTransactionDetails(transactionId).toDomain()
        })
    }

    override suspend fun updateTransaction(transactionInput: TransactionInput): ApiResult<TransactionDomain> {
        val request = CreateTransactionRequest(
            accountId = transactionInput.accountId,
            categoryId = transactionInput.categoryId,
            amount = transactionInput.amount,
            transactionDate = transactionInput.transactionDate,
            comment = transactionInput.comment
        )
        return apiCallHelper.safeApiCall(block = {
            apiService.updateTransaction(transactionInput.transactionId, request).toDomain()
        })
    }

    override suspend fun deleteTransaction(transactionId: Int): ApiResult<Boolean> {
        val response = apiService.deleteTransaction(transactionId)
        return apiCallHelper.safeApiCall(block = {
            response.isSuccessful
        })
    }
}