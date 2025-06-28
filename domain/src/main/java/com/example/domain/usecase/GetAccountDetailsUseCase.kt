package com.example.domain.usecase

import com.example.domain.response.ApiResult
import com.example.domain.model.AccountDetailsDomain
import com.example.domain.repository.AccountRepository
import javax.inject.Inject
/**
 * UseCase для получения детальной информации о конкретном аккаунте.
 *
 * @param repository Репозиторий для работы с данными аккаунтов
 * @param id Числовой идентификатор аккаунта
 *
 * @return ApiResult<AccountDetailsDomain>:
 *         - Success с детализированными данными аккаунта
 *         - Error с информацией об ошибке (не найден, нет доступа и т.д.)
 *         - Loading во время выполнения запроса
 */
class GetAccountDetailsUseCase @Inject constructor(
    private val repository: AccountRepository
){
    suspend fun execute(id: Int): ApiResult<AccountDetailsDomain> {
        return repository.getAccountDetails(accountId = id)
    }
}