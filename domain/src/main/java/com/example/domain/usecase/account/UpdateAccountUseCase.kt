package com.example.domain.usecase.account

import com.example.domain.model.AccountDomain
import com.example.domain.repository.AccountRepository
import javax.inject.Inject

class UpdateAccountUseCase @Inject constructor(
    private val repository: AccountRepository
){
    suspend fun execute(id: Int,
                        currency: String,
                        name: String,
                        balance: String): AccountDomain {
        return repository.updateAccount(
            accountId = id,
            currency = currency,
            name = name,
            balance = balance
        )
    }
}