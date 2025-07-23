package com.example.data.repository

import com.example.data.dto.account.UpdateAccountRequest
import com.example.data.dto.account.toDomain
import com.example.data.dto.account.toEntity
import com.example.data.retrofit.AccountApiService
import com.example.data.room.dao.AccountDao
import com.example.data.room.entityes.AccountEntity
import com.example.data.safecaller.ApiCallHelper
import com.example.domain.model.AccountDetailsDomain
import com.example.domain.model.AccountDomain
import com.example.domain.model.AccountHistoryDomain
import com.example.domain.repository.AccountRepository
import com.example.domain.response.ApiResult
import java.util.Date
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
    private val apiCallHelper: ApiCallHelper,
    private val accountDao: AccountDao
) : AccountRepository {
    /**
     * Получает список всех счетов пользователя.
     * @return [ApiResult] со списком [AccountDomain] или ошибкой
     */
    override suspend fun getAccounts(): List<AccountDomain> {
        val result = accountDao.getAllAccounts().map { it.toDomain() }

        if(result.isNotEmpty()){
            return result
        }

        val apiResult = apiCallHelper.safeApiCall({ apiService.getAccounts() })
        return when (apiResult){
            is ApiResult.Success -> {
                accountDao.insertAccounts(apiResult.data.map { it.toEntity() })
                accountDao.getAllAccounts().map{ it.toDomain() }
            }
            else -> emptyList()
        }
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
    ): AccountDomain {
        val acc = accountDao.getAccountById(accountId)
        val updated = acc.copy(
            name = name,
            balance = balance,
            currency = currency,
            updatedAt = Date()
        )
        accountDao.updateAccount(updated)
        return updated.toDomain()
    }
    /**
     * Получает историю операций по счету.
     * @param accountId ID счета
     * @return [ApiResult] с [AccountHistoryDomain] или ошибкой
     */
    override suspend fun getAccountHistory(accountId: Int): AccountHistoryDomain {
        val cached = accountDao.getAccountHistory(accountId)
        if(cached != null){
            return cached.toDomain()
        }
        val apiResult = apiCallHelper.safeApiCall({ apiService.getAccountHistory(accountId) })
        return when (apiResult){
            is ApiResult.Success -> {
                accountDao.insertAccountHistory(apiResult.data.toEntity())
                accountDao.getAccountHistory(accountId)?.toDomain() ?: throw Exception("Can't find nothing")
            }
            else -> throw Exception("Can't find nothing")
        }
    }
    /**
     * Получает детальную информацию о счете.
     * @param accountId ID счета
     * @return [ApiResult] с [AccountDetailsDomain] или ошибкой
     */
    override suspend fun getAccountDetails(accountId: Int): AccountDetailsDomain {
        val cached = accountDao.getAccountDetails(accountId)
        if(cached != null){
            return cached.toDomain()
        }
        val apiResult = apiCallHelper.safeApiCall({apiService.getAccountDetails(accountId)})
        return when (apiResult){
            is ApiResult.Success -> {
                accountDao.insertAccountDetails(apiResult.data.toEntity())
                accountDao.getAccountDetails(accountId)?.toDomain() ?: throw Exception("Can't find nothing")
            }
            else  -> throw Exception("Can't find nothing")
        }
    }


    suspend fun syncPendingData() {
        val localAccounts = accountDao.getAllAccounts().filter { it.updatedAt != null }

        localAccounts.forEach { localAccount ->
            try {
                val apiResult = apiCallHelper.safeApiCall({
                    apiService.getAccountDetails(localAccount.id)
                })

                when (apiResult) {
                    is ApiResult.Success -> {
                        val serverAccount = apiResult.data.toEntity()

                        val finalAccount = if (localAccount.updatedAt != null && serverAccount.updatedAt != null) {
                            val localUpdated = localAccount.updatedAt
                            val serverUpdated = serverAccount.updatedAt

                            if (localUpdated > serverUpdated) {
                                localAccount
                            } else {
                                AccountEntity(
                                    id = serverAccount.id,
                                    userId = localAccount.userId,
                                    name = serverAccount.name,
                                    balance = serverAccount.balance,
                                    currency = serverAccount.currency,
                                    createdAt = serverAccount.createdAt,
                                    updatedAt = serverAccount.updatedAt
                                )
                            }
                        } else {
                            localAccount
                        }

                        if (finalAccount == localAccount) {
                            apiCallHelper.safeApiCall ({
                                apiService.updateAccount(accountId = localAccount.id,
                                    request = UpdateAccountRequest(
                                        name = localAccount.name,
                                        balance = localAccount.balance,
                                        currency = localAccount.currency
                                    )
                                )
                            })
                        }
                        accountDao.updateAccount(finalAccount)
                    }
                    else -> {}
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}