package com.example.domain.repository

import com.example.domain.response.ApiResult
import com.example.domain.model.AccountDetailsDomain
import com.example.domain.model.AccountDomain
import com.example.domain.model.AccountHistoryDomain
/**
 * Репозиторий для работы с аккаунтами.
 * Предоставляет методы для
 * - Получения всех аккаунтов
 * - Создания нового аккаунта
 * - Обновлния информации по выбранному аккаунту
 * - Удаления аккаунта
 * - Получения истории транзакций аккаунта
 * - Получения детализрованной информации по аккаунту
 *
 * Все методы возвращают ApiResult с соответствующим типом данных.
 */
interface AccountRepository {
    /**
     * Получает список всех аккаунтов пользователя
     * @return ApiResult с:
     *         - Success<List<AccountDomain>> при успешном получении
     *         - Error с ApiError при ошибке
     *         - Loading во время выполнения запроса
     */
    suspend fun getAccounts(): ApiResult<List<AccountDomain>>
    /**
     * Создает новый аккаунт
     * @param name Название аккаунта
     * @param currency Валюта аккаунта (код валюты, например "RUB")
     * @return ApiResult с созданным AccountDomain или ошибкой
     */
    suspend fun createAccount(name: String, currency: String): ApiResult<AccountDomain>
    /**
     * Обновляет данные аккаунта
     * @param accountId ID редактируемого аккаунта
     * @param name Новое название (опционально)
     * @param balance Новый баланс (опционально, строковое представление)
     * @param currency Новая валюта (опционально)
     * @return ApiResult с обновленным AccountDomain или ошибкой
     */
    suspend fun updateAccount(
        accountId: Int,
        name: String? = null,
        balance: String? = null,
        currency: String? = null
    ): ApiResult<AccountDomain>
    /**
     * Удаляет аккаунт
     * @param accountId ID удаляемого аккаунта
     * @return ApiResult с Boolean:
     *         - true при успешном удалении
     *         - Error с ApiError при ошибке
     */
    suspend fun deleteAccount(accountId: Int): ApiResult<Boolean>
    /**
     * Получает историю операций и информацию об аккаунте по id аккаунта
     * @param accountId ID аккаунта
     * @return ApiResult с AccountHistoryDomain (содержит историю операций) или ошибкой
     */
    suspend fun getAccountHistory(accountId: Int): ApiResult<AccountHistoryDomain>
    /**
     * Получает детальную информацию об аккаунте
     * @param accountId ID аккаунта
     * @return ApiResult с AccountDetailsDomain (расширенная информация) или ошибкой
     */
    suspend fun getAccountDetails(accountId: Int): ApiResult<AccountDetailsDomain>
}