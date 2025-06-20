package com.example.showmethemoney.data.dto.account

import com.example.showmethemoney.data.dto.category.toDomain
import com.example.showmethemoney.domain.AccountDetailsDomain
import com.example.showmethemoney.domain.AccountDomain
import com.example.showmethemoney.domain.AccountHistoryDomain
import com.example.showmethemoney.domain.AccountHistoryItemDomain

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

fun AccountHistoryResponse.toDomain(): AccountHistoryDomain {
    return AccountHistoryDomain(
        accountId = accountId.toString(),
        accountName = accountName,
        currency = currency,
        currentBalance = currentBalance,
        history = history.map { it.toDomain() }
    )
}

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