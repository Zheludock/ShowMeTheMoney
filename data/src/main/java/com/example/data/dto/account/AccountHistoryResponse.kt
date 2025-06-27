package com.example.data.dto.account
/**
 * Ответ API с историей изменений аккаунта.
 * @property accountId Идентификатор аккаунта.
 * @property accountName Название аккаунта.
 * @property currency Валюта аккаунта (например, "USD").
 * @property currentBalance Текущий баланс в виде строки (для точности).
 * @property history Список изменений аккаунта.
 */
data class AccountHistoryResponse(
    val accountId: Int,
    val accountName: String,
    val currency: String,
    val currentBalance: String,
    val history: List<AccountHistoryItem>
)