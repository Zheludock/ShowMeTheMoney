package com.example.data.dto.transaction

import android.util.Log
import com.example.domain.model.CreateTransactionDomain
import com.example.domain.model.TransactionDomain
/**
 * Преобразует DTO транзакции из API в доменную модель.
 * Выполняет:
 * - Конвертацию типов (Int → String для ID)
 * - Извлечение необходимых данных из вложенных объектов
 * - Определение типа транзакции (доход/расход)
 *
 * @return [TransactionDomain] - доменная модель транзакции
 */
fun TransactionResponse.toDomain(): TransactionDomain {
    Log.d("Mapper started", "Start")
    return TransactionDomain(
        id = id,
        emoji = category.emoji,
        categoryName = category.name,
        categoryId = category.id,
        amount = amount,
        transactionDate = transactionDate,
        comment = comment,
        currency = account.currency,
        createdAt = createdAt,
        updatedAt = updatedAt,
        isIncome = category.isIncome
    )
}

fun CreateTransactionResponse.toDomain(): CreateTransactionDomain {
    return CreateTransactionDomain(
        id = id,
        accountId = accountId,
        categoryId = categoryId,
        amount = amount,
        transactionDate = transactionDate,
        comment = comment,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}