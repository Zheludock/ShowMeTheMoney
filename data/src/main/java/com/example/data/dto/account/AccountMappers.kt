package com.example.data.dto.account

import com.example.data.dto.category.toDomain
import com.example.domain.model.AccountDetailsDomain
import com.example.domain.model.AccountDomain
import com.example.domain.model.AccountHistoryDomain
import com.example.domain.model.AccountHistoryItemDomain

/**
 * Преобразует данные аккаунта из DTO API в доменную модель.
 *
 * @receiver [AccountResponse] DTO аккаунта, полученное от сервера
 * @return [AccountDomain] Доменная модель аккаунта
 */
fun AccountResponse.toDomain(): AccountDomain {
    return AccountDomain(
        id = id.toString(),
        userId = userId.toString(),
        name = name,
        balance = balance,
        currency = currency,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
/**
 * Преобразует детализированные данные аккаунта (включая статистику доходов/расходов)
 * из DTO API в доменную модель.
 *
 * @receiver [AccountDetailsResponse] DTO детализированной информации об аккаунте
 * @return [AccountDetailsDomain] Доменная модель с детализированной информацией об аккаунте
 */
fun AccountDetailsResponse.toDomain(): AccountDetailsDomain {
    return AccountDetailsDomain(
        id = id.toString(),
        name = name,
        balance = balance,
        currency = currency,
        incomeStats = incomeStats.map { it.toDomain() },
        expenseStats = expenseStats.map { it.toDomain() },
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
/**
 * Преобразует элемент истории изменений аккаунта из DTO API в доменную модель.
 *
 * @receiver [AccountHistoryItem] DTO элемента истории изменений аккаунта
 * @return [AccountHistoryItemDomain] Доменная модель элемента истории изменений
 */
fun AccountHistoryItem.toDomain(): AccountHistoryItemDomain {
    return AccountHistoryItemDomain(
        id = id.toString(),
        accountId = accountId.toString(),
        changeType = changeType,
        previousState = previousState?.toDomain(),
        newState = newState.toDomain(),
        changeTimestamp = changeTimestamp,
        createdAt = createdAt
    )
}
/**
 * Преобразует полную историю изменений аккаунта из DTO API в доменную модель.
 *
 * @receiver [AccountHistoryResponse] DTO истории изменений аккаунта
 * @return [AccountHistoryDomain] Доменная модель истории изменений аккаунта
 */
fun AccountHistoryResponse.toDomain(): AccountHistoryDomain {
    return AccountHistoryDomain(
        accountId = accountId.toString(),
        accountName = accountName,
        currency = currency,
        currentBalance = currentBalance,
        history = history.map { it.toDomain() }
    )
}
/**
 * Преобразует базовую информацию об аккаунте (используемую для статистики)
 * из DTO API в доменную модель.
 *
 * @receiver [AccountInfo] DTO базовой информации об аккаунте
 * @return [AccountDomain] Упрощенная доменная модель аккаунта для статистических целей
 */
fun AccountInfo.toDomain(): AccountDomain {
    return AccountDomain(
        id = id.toString(),
        userId = "",
        name = name,
        balance = balance,
        currency = currency,
        createdAt = "",
        updatedAt = ""
    )
}