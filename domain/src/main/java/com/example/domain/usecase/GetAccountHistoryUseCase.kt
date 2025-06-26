package com.example.domain.usecase

import com.example.domain.ApiResult
import com.example.domain.model.AccountHistoryDomain
import com.example.domain.repository.AccountRepository
import javax.inject.Inject

class GetAccountHistoryUseCase @Inject constructor(
    private val repository: AccountRepository
){
    suspend fun execute(accountId: String): ApiResult<AccountHistoryDomain> {
        return repository.getAccountHistory(accountId = accountId.toInt())
    }
}