package com.example.data

import com.example.data.dto.account.CreateAccountRequest
import com.example.data.dto.account.toDomain
import com.example.data.safecaller.ApiCallHelper
import com.example.domain.ApiResult
import com.example.domain.model.AccountDetailsDomain
import com.example.domain.model.AccountDomain
import com.example.domain.model.AccountHistoryDomain
import com.example.domain.repository.AccountRepository
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val apiService: AccountApiService,
    private val apiCallHelper: ApiCallHelper
) : AccountRepository {

    override suspend fun getAccounts(): ApiResult<List<AccountDomain>> {
        return apiCallHelper.safeApiCall(block = {
            apiService.getAccounts().map { it.toDomain() }
        })
    }

    override suspend fun createAccount(name: String, currency: String): ApiResult<AccountDomain> {
        return apiCallHelper.safeApiCall(block = {apiService.createAccount(
            CreateAccountRequest(
                name,
                currency)
                ).toDomain()
            }
        )
    }

    override suspend fun updateAccount(
        accountId: Int,
        name: String?,
        balance: String?,
        currency: String?
    ): ApiResult<AccountDomain> {
        val request = com.example.data.dto.account.UpdateAccountRequest(name, balance, currency)
        return apiCallHelper.safeApiCall(block = {
            apiService.updateAccount(accountId, request).toDomain()
        })
    }

    override suspend fun deleteAccount(accountId: Int): ApiResult<Boolean> {
        val response = apiService.deleteAccount(accountId)
        return apiCallHelper.safeApiCall(block = {
            response.isSuccessful
        })
    }

    override suspend fun getAccountHistory(accountId: Int): ApiResult<AccountHistoryDomain> {
        return apiCallHelper.safeApiCall(block = {
            apiService.getAccountHistory(accountId).toDomain()
        })
    }

    override suspend fun getAccountDetails(accountId: Int): ApiResult<AccountDetailsDomain> {
        return apiCallHelper.safeApiCall(block = {
            apiService.getAccountDetails(accountId).toDomain()
        })
    }
}