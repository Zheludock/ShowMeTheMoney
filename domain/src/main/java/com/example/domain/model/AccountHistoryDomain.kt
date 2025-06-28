package com.example.domain.model
/**
 * Доменная модель истории изменений аккаунта.
 * Содержит текущее состояние аккаунта и список всех изменений.
 *
 * @property accountId Уникальный идентификатор аккаунта
 * @property accountName Название аккаунта для отображения
 * @property currency Валюта аккаунта (3-буквенный код ISO: "USD", "EUR", "RUB")
 * @property currentBalance Текущий баланс
 * @property history Список изменений аккаунта
 */
data class AccountHistoryDomain(
    val accountId: String,
    val accountName: String,
    val currency: String,
    val currentBalance: String,
    val history: List<AccountHistoryItemDomain>
)