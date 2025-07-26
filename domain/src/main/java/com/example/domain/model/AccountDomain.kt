package com.example.domain.model

import java.util.Date

/**
 * Доменная модель банковского аккаунта пользователя.
 * Содержит основную информацию о счете и его текущем состоянии.
 *
 * @property id Уникальный идентификатор аккаунта
 * @property userId Идентификатор владельца аккаунта
 * @property name Название счета для отображения
 * @property balance Текущий баланс в строковом формате
 * @property currency Валюта счета (3-буквенный код ISO: "RUB", "USD", "EUR")
 * @property createdAt Дата создания счета
 * @property updatedAt Дата последнего обновления
 */
data class AccountDomain(
    val id: Int,
    val userId: Int,
    var name: String,
    var balance : String,
    var currency: String,
    val createdAt: Date,
    val updatedAt: Date?,
)
