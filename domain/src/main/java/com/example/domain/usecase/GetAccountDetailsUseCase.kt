package com.example.domain.usecase

import com.example.domain.ApiResult
import com.example.domain.model.AccountDetailsDomain
import com.example.domain.repository.AccountRepository
import javax.inject.Inject

class GetAccountDetailsUseCase @Inject constructor(
    private val repository: AccountRepository
){
    suspend fun execute(id: Int): ApiResult<AccountDetailsDomain> {
        return repository.getAccountDetails(accountId = id)
    }
}