package com.example.ui

import com.example.domain.model.TransactionDomain

/**
 * Данные финансовой транзакции.
 *
 * @param id Уникальный идентификатор транзакции.
 * @param categoryEmoji Эмодзи категории транзакции (например, "🍔").
 * @param categoryName Название категории (например, "Еда").
 * @param comment Комментарий к транзакции (может быть null).
 * @param amount Сумма транзакции в виде строки (например, "150.50").
 * @param accountCurrency Валюта счёта (например, "RUB").
 * @param createdAt Дата создания в формате строки (опционально).
 * @param isIncome true — доход, false — расход.
 */
data class TransactionItem(
    val id: Int,
    val categoryEmoji: String,
    val categoryName: String,
    val categoryId: Int,
    val comment: String?,
    val amount: String,
    val accountCurrency: String,
    val createdAt: String,
    val isIncome: Boolean
)
fun TransactionDomain.toTransactionItem(): TransactionItem {
    return TransactionItem(
        id = id,
        categoryEmoji = emoji,
        categoryName = categoryName,
        categoryId = categoryId,
        comment = comment,
        amount = amount,
        accountCurrency = currency,
        createdAt = createdAt,
        isIncome = isIncome
    )
}