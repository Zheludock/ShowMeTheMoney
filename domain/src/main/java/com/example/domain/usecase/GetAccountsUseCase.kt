package com.example.domain.usecase

import com.example.domain.ApiResult
import com.example.domain.model.AccountDomain
import com.example.domain.repository.AccountRepository
import javax.inject.Inject

class GetAccountsUseCase @Inject constructor(
    private val repository: AccountRepository
){
    suspend fun execute(): ApiResult<List<AccountDomain>> {
        return repository.getAccounts()
    }
}