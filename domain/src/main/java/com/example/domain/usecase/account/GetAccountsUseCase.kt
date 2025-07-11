package com.example.domain.usecase.account

import com.example.domain.response.ApiResult
import com.example.domain.model.AccountDomain
import com.example.domain.repository.AccountRepository
import javax.inject.Inject
/**
 * UseCase для получения списка всех аккаунтов пользователя.
 *
 * @param repository Репозиторий для работы с аккаунтами
 *
 * Возвращает:
 * - Success с List<AccountDomain> при успешном получении данных
 * - Error с ApiError в случае возникновения ошибки
 * - Loading при выполнении запроса
 *
 */
class GetAccountsUseCase @Inject constructor(
    private val repository: AccountRepository
){
    suspend fun execute(): ApiResult<List<AccountDomain>> {
        return repository.getAccounts()
    }
}