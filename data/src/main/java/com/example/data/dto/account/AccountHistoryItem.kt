package com.example.data.dto.account

import java.util.Date

/**
 * Элемент истории изменений аккаунта.
 * @property id Уникальный идентификатор изменения.
 * @property accountId Идентификатор аккаунта.
 * @property changeType Тип изменения (например, "DEPOSIT", "WITHDRAWAL").
 * @property previousState Состояние до изменения (null для создания аккаунта).
 * @property newState Состояние после изменения.
 * @property changeTimestamp Время изменения.
 * @property createdAt Время создания записи в истории.
 */
data class AccountHistoryItem(
    val id: Int,
    val accountId: Int,
    val changeType: String,
    val previousState: AccountInfo?,
    val newState: AccountInfo,
    val changeTimestamp: Date,
    val createdAt: Date
)