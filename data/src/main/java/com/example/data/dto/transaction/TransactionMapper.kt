package com.example.data.dto.transaction

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
    return TransactionDomain(
        id = id.toString(),
        emoji = category.emoji,
        categoryName = category.name,
        amount = amount,
        transactionDate = transactionDate,
        comment = comment,
        currency = account.currency,
        createdAt = createdAt,
        updatedAt = updatedAt,
        isIncome = category.isIncome
    )
}