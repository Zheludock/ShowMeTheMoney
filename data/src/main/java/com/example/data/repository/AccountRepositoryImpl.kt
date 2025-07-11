package com.example.data.repository

import com.example.data.retrofit.AccountApiService
import com.example.data.dto.account.CreateAccountRequest
import com.example.data.dto.account.UpdateAccountRequest
import com.example.data.dto.account.toDomain
import com.example.data.safecaller.ApiCallHelper
import com.example.domain.response.ApiResult
import com.example.domain.model.AccountDetailsDomain
import com.example.domain.model.AccountDomain
import com.example.domain.model.AccountHistoryDomain
import com.example.domain.repository.AccountRepository
import javax.inject.Inject
/**
 * Реализация [AccountRepository] для работы с банковскими счетами через API.
 * Обрабатывает все операции со счетами: создание, чтение, обновление, удаление,
 * а также получение истории операций и деталей счета.
 *
 * @param apiService API для работы со счетами
 * @param apiCallHelper Инструмент для безопасных вызовов API с обработкой ошибок. Все обращения
 *                                                 к сети выполнять только внутри apiCallHelper!
 */
class AccountRepositoryImpl @Inject constructor(
    private val apiService: AccountApiService,
    private val apiCallHelper: ApiCallHelper
) : AccountRepository {
    /**
     * Получает список всех счетов пользователя.
     * @return [ApiResult] со списком [AccountDomain] или ошибкой
     */
    override suspend fun getAccounts(): ApiResult<List<AccountDomain>> {
        return apiCallHelper.safeApiCall(block = {
            apiService.getAccounts().map { it.toDomain() }
        })
    }
    /**
     * Создает новый счет.
     * @param name Название счета
     * @param currency Валюта счета (3-буквенный код, например "RUB")
     * @return [ApiResult] с созданным [AccountDomain] или ошибкой
     */
    override suspend fun createAccount(name: String, currency: String): ApiResult<AccountDomain> {
        return apiCallHelper.safeApiCall(block = {apiService.createAccount(
            CreateAccountRequest(
                name,
                currency)
                ).toDomain()
            }
        )
    }
    /**
     * Обновляет существующий счет.
     * @param accountId ID счета
     * @param name Новое название (опционально)
     * @param balance Новый баланс (опционально)
     * @param currency Новая валюта (опционально)
     * @return [ApiResult] с обновленным [AccountDomain] или ошибкой
     */
    override suspend fun updateAccount(
        accountId: Int,
        name: String,
        balance: String,
        currency: String
    ): ApiResult<AccountDomain> {
        return apiCallHelper.safeApiCall(block = {
            val request = UpdateAccountRequest(name, balance, currency)
            apiService.updateAccount(accountId, request).toDomain()
        })
    }
    /**
     * Удаляет счет.
     * @param accountId ID счета для удаления
     * @return [ApiResult] с результатом операции (true/false) или ошибкой
     */
    override suspend fun deleteAccount(accountId: Int): ApiResult<Boolean> {
        return apiCallHelper.safeApiCall(block = {
            val response = apiService.deleteAccount(accountId)
            response.isSuccessful
        })
    }
    /**
     * Получает историю операций по счету.
     * @param accountId ID счета
     * @return [ApiResult] с [AccountHistoryDomain] или ошибкой
     */
    override suspend fun getAccountHistory(accountId: Int): ApiResult<AccountHistoryDomain> {
        return apiCallHelper.safeApiCall(block = {
            apiService.getAccountHistory(accountId).toDomain()
        })
    }
    /**
     * Получает детальную информацию о счете.
     * @param accountId ID счета
     * @return [ApiResult] с [AccountDetailsDomain] или ошибкой
     */
    override suspend fun getAccountDetails(accountId: Int): ApiResult<AccountDetailsDomain> {
        return apiCallHelper.safeApiCall(block = {
            apiService.getAccountDetails(accountId).toDomain()
        })
    }
}