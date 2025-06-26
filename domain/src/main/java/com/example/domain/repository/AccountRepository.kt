package com.example.domain.repository

import com.example.domain.ApiResult
import com.example.domain.model.AccountDetailsDomain
import com.example.domain.model.AccountDomain
import com.example.domain.model.AccountHistoryDomain

interface AccountRepository {

    suspend fun getAccounts(): ApiResult<List<AccountDomain>>

    suspend fun createAccount(name: String, currency: String): ApiResult<AccountDomain>

    suspend fun updateAccount(
        accountId: Int,
        name: String? = null,
        balance: String? = null,
        currency: String? = null
    ): ApiResult<AccountDomain>

    suspend fun deleteAccount(accountId: Int): ApiResult<Boolean>

    suspend fun getAccountHistory(accountId: Int): ApiResult<AccountHistoryDomain>

    suspend fun getAccountDetails(accountId: Int): ApiResult<AccountDetailsDomain>
}