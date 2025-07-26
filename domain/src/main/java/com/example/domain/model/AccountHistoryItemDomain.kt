package com.example.domain.model

import java.util.Date

/**
 * Доменная модель элемента истории изменений аккаунта.
 * Содержит информацию об одном изменении состояния аккаунта.
 *
 * @property id Уникальный идентификатор записи истории
 * @property accountId Идентификатор аккаунта, к которому относится изменение
 * @property changeType Тип изменения (см. [AccountChangeType])
 * @property previousState Состояние аккаунта до изменения (null для первой записи)
 * @property newState Состояние аккаунта после изменения
 * @property changeTimestamp Времення изменения
 * @property createdAt Времення создания записи
 */
data class AccountHistoryItemDomain(
    val id: Int,
    val accountId: Int,
    val changeType: String,
    val previousState: AccountDomain?,
    val newState: AccountDomain,
    val changeTimestamp: Date?,
    val createdAt: Date
)