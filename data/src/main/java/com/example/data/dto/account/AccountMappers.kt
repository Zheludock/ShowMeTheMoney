package com.example.data.dto.account

import com.example.data.dto.category.CategoryStats
import com.example.data.dto.category.toDomain
import com.example.data.room.entityes.AccountDetailsEntity
import com.example.data.room.entityes.AccountDetailsWithStats
import com.example.data.room.entityes.AccountEntity
import com.example.data.room.entityes.AccountHistoryEntity
import com.example.data.room.entityes.AccountHistoryItemEntity
import com.example.data.room.entityes.AccountHistoryWithItems
import com.example.data.room.entityes.CategoryStatsEntity
import com.example.domain.model.AccountDetailsDomain
import com.example.domain.model.AccountDomain
import com.example.domain.model.AccountHistoryDomain
import com.example.domain.model.AccountHistoryItemDomain
import com.example.domain.model.CategoryStatsDomain
import com.google.gson.Gson

val gson = Gson()

fun AccountInfo.toJson(): String = gson.toJson(this)

fun String.toAccountDomain(): AccountDomain = gson.fromJson(this, AccountDomain::class.java)


/**
 * Преобразует данные аккаунта из DTO API в доменную модель.
 *
 * @receiver [AccountResponse] DTO аккаунта, полученное от сервера
 * @return [AccountDomain] Доменная модель аккаунта
 */
fun AccountResponse.toDomain(): AccountDomain {
    return AccountDomain(
        id = id,
        userId = userId,
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
        id = id,
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
        id = id,
        accountId = accountId,
        changeType = changeType,
        previousState = previousState?.toAccountDomain(),
        newState = newState.toAccountDomain(),
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
        accountId = accountId,
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
fun AccountInfo.toAccountDomain(): AccountDomain {
    return AccountDomain(
        id = id,
        userId = -1,
        name = name,
        balance = balance,
        currency = currency,
        createdAt = "",
        updatedAt = ""
    )
}

fun AccountResponse.toEntity() = AccountEntity(
    id = id,
    userId = userId,
    name = name,
    balance = balance,
    currency = currency,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun AccountEntity.toDomain() = AccountDomain(
    id = id,
    userId = userId,
    name = name,
    balance = balance,
    currency = currency,
    createdAt = createdAt,
    updatedAt = updatedAt
)

// AccountHistory

fun AccountHistoryResponse.toEntity() = AccountHistoryEntity(
    accountId = accountId,
    accountName = accountName,
    currency = currency,
    currentBalance = currentBalance
)

fun AccountHistoryItem.toEntity(): AccountHistoryItemEntity = AccountHistoryItemEntity(
    id = id,
    accountId = accountId,
    changeType = changeType,
    previousStateJson = previousState?.toJson(),
    newStateJson = newState.toJson(),
    changeTimestamp = changeTimestamp,
    createdAt = createdAt
)

fun AccountHistoryWithItems.toDomain(): AccountHistoryDomain {
    return AccountHistoryDomain(
        accountId = history.accountId,
        accountName = history.accountName,
        currency = history.currency,
        currentBalance = history.currentBalance,
        history = items.map { it.toDomain() }
    )
}

fun AccountHistoryItemEntity.toDomain(): AccountHistoryItemDomain {
    return AccountHistoryItemDomain(
        id = id,
        accountId = accountId,
        changeType = changeType,
        previousState = previousStateJson?.toAccountDomain(),
        newState = newStateJson.toAccountDomain(),
        changeTimestamp = changeTimestamp,
        createdAt = createdAt
    )
}

fun AccountDetailsResponse.toEntity() = AccountDetailsEntity(
    id = id,
    name = name,
    balance = balance,
    currency = currency,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun CategoryStats.toEntity(accountId: Int, isIncome: Boolean) = CategoryStatsEntity(
    accountId = accountId,
    categoryId = categoryId,
    categoryName = categoryName,
    emoji = emoji,
    amount = amount,
    isIncome = isIncome
)

fun AccountDetailsWithStats.toDomain(): AccountDetailsDomain {
    return AccountDetailsDomain(
        id = details.id,
        name = details.name,
        balance = details.balance,
        currency = details.currency,
        incomeStats = stats.filter { it.isIncome }.map { CategoryStatsDomain(
            categoryId = it.categoryId,
            categoryName = it.categoryName,
            emoji = it.emoji,
            amount = it.amount
        ) },
        expenseStats = stats.filter { !it.isIncome }.map { CategoryStatsDomain(
            categoryId = it.categoryId,
            categoryName = it.categoryName,
            emoji = it.emoji,
            amount = it.amount
        ) },
        createdAt = details.createdAt,
        updatedAt = details.updatedAt
    )
}