package com.example.domain.repository

import com.example.domain.ApiResult
import com.example.domain.model.TransactionDomain
import com.example.domain.model.TransactionInput

interface TransactionRepository {

    suspend fun createTransaction(
        accountId: Int,
        categoryId: String,
        amount: String,
        transactionDate: String,
        comment: String? = null
    ): ApiResult<TransactionDomain>

    suspend fun getTransactionDetails(transactionId: Int): ApiResult<TransactionDomain>

    suspend fun updateTransaction(transactionInput: TransactionInput): ApiResult<TransactionDomain>

    suspend fun deleteTransaction(transactionId: Int): ApiResult<Boolean>

    suspend fun getTransactions(
        accountId: Int,
        startDate: String? = null,
        endDate: String? = null
    ): ApiResult<List<TransactionDomain>>
}