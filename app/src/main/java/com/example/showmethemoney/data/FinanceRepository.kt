package com.example.showmethemoney.data

import com.example.showmethemoney.data.dto.account.AccountDetailsResponse
import com.example.showmethemoney.data.dto.account.AccountHistoryResponse
import com.example.showmethemoney.data.dto.account.AccountResponse
import com.example.showmethemoney.data.dto.account.CreateAccountRequest
import com.example.showmethemoney.data.dto.account.UpdateAccountRequest
import com.example.showmethemoney.data.dto.category.CategoryResponse
import com.example.showmethemoney.data.dto.transaction.CreateTransactionRequest
import com.example.showmethemoney.data.dto.transaction.TransactionDetailsResponse
import com.example.showmethemoney.data.dto.transaction.TransactionResponse

class FinanceRepository {

    private val apiService = RetrofitClient.apiService

    suspend fun getAccounts(): List<AccountResponse> {
        return apiService.getAccounts()
    }

    suspend fun createAccount(name: String, currency: String): AccountResponse {
        return apiService.createAccount(CreateAccountRequest(name, currency))
    }

    suspend fun getTransactions(
        accountId: Int,
        startDate: String? = null,
        endDate: String? = null
    ): List<TransactionResponse> {
        return apiService.getTransactions(accountId, startDate, endDate)
    }

    suspend fun getAccountDetails(accountId: Int): AccountDetailsResponse {
        return apiService.getAccountDetails(accountId)
    }

    suspend fun updateAccount(
        accountId: Int,
        name: String? = null,
        balance: String? = null,
        currency: String? = null
    ): AccountResponse {
        val request = UpdateAccountRequest(name, balance, currency)
        return apiService.updateAccount(accountId, request)
    }

    suspend fun deleteAccount(accountId: Int): Boolean {
        val response = apiService.deleteAccount(accountId)
        return response.isSuccessful
    }

    suspend fun getAccountHistory(accountId: Int): AccountHistoryResponse {
        return apiService.getAccountHistory(accountId)
    }

    suspend fun getAllCategories(): List<CategoryResponse> {
        return apiService.getAllCategories()
    }

    suspend fun getCategoriesByType(isIncome: Boolean): List<CategoryResponse> {
        return apiService.getCategoriesByType(isIncome)
    }

    suspend fun createTransaction(
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String? = null
    ): TransactionDetailsResponse {
        val request = CreateTransactionRequest(
            accountId = accountId,
            categoryId = categoryId,
            amount = amount,
            transactionDate = transactionDate,
            comment = comment
        )
        return apiService.createTransaction(request)
    }

    suspend fun getTransactionDetails(transactionId: Int): TransactionDetailsResponse {
        return apiService.getTransactionDetails(transactionId)
    }

    suspend fun updateTransaction(
        transactionId: Int,
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String? = null
    ): TransactionDetailsResponse {
        val request = CreateTransactionRequest(
            accountId = accountId,
            categoryId = categoryId,
            amount = amount,
            transactionDate = transactionDate,
            comment = comment
        )
        return apiService.updateTransaction(transactionId, request)
    }

    suspend fun deleteTransaction(transactionId: Int): Boolean {
        val response = apiService.deleteTransaction(transactionId)
        return response.isSuccessful
    }
}