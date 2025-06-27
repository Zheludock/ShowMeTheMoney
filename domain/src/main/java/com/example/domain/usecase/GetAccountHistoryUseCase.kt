package com.example.domain.usecase

import com.example.domain.response.ApiResult
import com.example.domain.model.AccountHistoryDomain
import com.example.domain.repository.AccountRepository
import javax.inject.Inject
/**
 * UseCase для получения истории операций по конкретному аккаунту.
 *
 * @param repository Репозиторий для работы с данными аккаунтов
 *
 * @param accountId Идентификатор аккаунта в строковом формате (конвертируется в Int)
 *
 * @return ApiResult<AccountHistoryDomain>:
 *         - Success с данными истории операций
 *         - Error с информацией об ошибке
 *         - Loading во время выполнения запроса
 */
class GetAccountHistoryUseCase @Inject constructor(
    private val repository: AccountRepository
){
    suspend fun execute(accountId: String): ApiResult<AccountHistoryDomain> {
        return repository.getAccountHistory(accountId = accountId.toInt())
    }
}